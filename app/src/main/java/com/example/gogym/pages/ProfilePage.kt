package com.example.gogym.pages

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gogym.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//  DATA + VIEWMODEL

enum class AppLanguage { VI, EN }
enum class WeightUnit { KG, LBS }
enum class HeightUnit { CM, INCH }

// Trạng thái dùng cho UI
data class ProfileUiState(
    val age: Int? = null,
    val heightCm: Int? = null,
    val weightKg: Int? = null,
    val goal: String? = null,                      // mục tiêu luyện tập (sau này bạn có thể thêm UI)
    val weightUnit: WeightUnit = WeightUnit.KG,    // kg / lbs
    val heightUnit: HeightUnit = HeightUnit.CM,    // cm / inch
    val language: AppLanguage = AppLanguage.VI,
    val isLoading: Boolean = false
)

// Dữ liệu map lên Firestore
data class UserProfile(
    val age: Int? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val goal: String? = null,
    val weightUnit: String? = null,   // "kg" / "lbs"
    val heightUnit: String? = null,   // "cm" / "inch"
    val language: String? = null      // "vi" / "en"
)

class ProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    // Collection "users" / mỗi user có subcollection "profile" / doc "main"
    private fun profileDoc() =
        auth.currentUser?.uid?.let { uid ->
            firestore.collection("users")
                .document(uid)
                .collection("profile")
                .document("main")
        }

    // GỌI KHI VÀO MÀN HỒ SƠ → LOAD TỪ FIRESTORE
    fun loadProfile() {
        val docRef = profileDoc() ?: return

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val snapshot = docRef.get().await()
                if (snapshot.exists()) {
                    snapshot.toObject(UserProfile::class.java)?.let { fromRemoteToUi(it) }
                }
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    // LƯU LÊN FIRESTORE SAU KHI CẬP NHẬT UI
    private fun saveProfile() {
        val docRef = profileDoc() ?: return
        val current = _uiState.value

        val remote = UserProfile(
            age = current.age,
            height = current.heightCm,
            weight = current.weightKg,
            goal = current.goal,
            weightUnit = when (current.weightUnit) {
                WeightUnit.KG -> "kg"
                WeightUnit.LBS -> "lbs"
            },
            heightUnit = when (current.heightUnit) {
                HeightUnit.CM -> "cm"
                HeightUnit.INCH -> "inch"
            },
            language = when (current.language) {
                AppLanguage.VI -> "vi"
                AppLanguage.EN -> "en"
            }
        )

        docRef.set(remote)
            .addOnSuccessListener {
                // Lưu thành công
            }
            .addOnFailureListener {
                // Lưu thất bại
            }
    }

    // Map từ Firestore → UI
    private fun fromRemoteToUi(profile: UserProfile) {
        val ui = ProfileUiState(
            age = profile.age,
            heightCm = profile.height,
            weightKg = profile.weight,
            goal = profile.goal,
            weightUnit = when (profile.weightUnit?.lowercase()) {
                "lbs" -> WeightUnit.LBS
                else -> WeightUnit.KG
            },
            heightUnit = when (profile.heightUnit?.lowercase()) {
                "inch" -> HeightUnit.INCH
                else -> HeightUnit.CM
            },
            language = when (profile.language?.lowercase()) {
                "en" -> AppLanguage.EN
                else -> AppLanguage.VI
            }
        )
        _uiState.value = ui
    }

    // ======= UPDATE UI + TỰ ĐỘNG LƯU FIRESTORE =======

    fun updateAge(v: Int?) {
        _uiState.value = _uiState.value.copy(age = v)
        saveProfile()
    }

    fun updateHeight(v: Int?) {
        _uiState.value = _uiState.value.copy(heightCm = v)
        saveProfile()
    }

    fun updateWeight(v: Int?) {
        _uiState.value = _uiState.value.copy(weightKg = v)
        saveProfile()
    }

    fun updateGoal(goal: String?) {
        _uiState.value = _uiState.value.copy(goal = goal)
        saveProfile()
    }

    fun updateWeightUnit(unit: WeightUnit) {
        _uiState.value = _uiState.value.copy(weightUnit = unit)
        saveProfile()
    }

    fun updateHeightUnit(unit: HeightUnit) {
        _uiState.value = _uiState.value.copy(heightUnit = unit)
        saveProfile()
    }

    fun updateLanguage(v: AppLanguage) {
        _uiState.value = _uiState.value.copy(language = v)
        saveProfile()
    }
}

// ================= PROFILE PAGE =================

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showAgeDialog by remember { mutableStateOf(false) }
    var showHeightDialog by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    val accent = Color(0xFF7C4DFF)

    // 🔹 Khi vào màn hình → load dữ liệu từ Firestore
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    Scaffold(
        containerColor = Color(0xFFF3F4F8)
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ===== Header + avatar =====
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF7C4DFF),
                                    Color(0xFFB388FF)
                                )
                            )
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.9f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "GG",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = accent
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Hồ sơ cá nhân",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Quản lý thông tin và cài đặt tài khoản Go Gym",
                                fontSize = 13.sp,
                                color = Color(0xFFEDE7FF)
                            )
                        }
                    }
                }
            }

            // ===== Card: Thông tin cá nhân =====
            item {
                ProfileSectionCard(title = "Thông tin của tôi") {
                    SettingRowWithIcon(
                        icon = Icons.Outlined.AccessTime,
                        title = "Tuổi",
                        value = uiState.age?.toString() ?: "---",
                        onClick = { showAgeDialog = true }
                    )
                    SettingRowWithIcon(
                        icon = Icons.Outlined.Straighten,
                        title = "Chiều cao",
                        value = uiState.heightCm?.let { "$it ${if (uiState.heightUnit == HeightUnit.CM) "cm" else "inch"}" } ?: "---",
                        onClick = { showHeightDialog = true }
                    )
                    SettingRowWithIcon(
                        icon = Icons.Outlined.FitnessCenter,
                        title = "Trọng lượng",
                        value = uiState.weightKg?.let { "$it ${if (uiState.weightUnit == WeightUnit.KG) "kg" else "lbs"}" } ?: "---",
                        onClick = { showWeightDialog = true }
                    )
                    // Sau này bạn có thể thêm 1 dòng "Mục tiêu luyện tập" ở đây
                }
            }

            // ===== Card: Cài đặt =====
            item {
                val langText = when (uiState.language) {
                    AppLanguage.VI -> "Tiếng Việt"
                    AppLanguage.EN -> "English"
                }

                ProfileSectionCard(title = "Cài đặt") {
                    SettingRowWithIcon(
                        icon = Icons.Outlined.Language,
                        title = "Ngôn ngữ",
                        value = langText,
                        onClick = { showLanguageDialog = true }
                    )
                }
            }

            // ===== Card: Hỗ trợ =====
            item {
                ProfileSectionCard(title = "Hỗ trợ") {
                    SettingRowWithIcon(
                        icon = Icons.Outlined.Report,
                        title = "Báo cáo vấn đề",
                        onClick = { reportProblem(context) }
                    )
                    SettingRowWithIcon(
                        icon = Icons.Outlined.Share,
                        title = "Chia sẻ ứng dụng",
                        onClick = { shareApp(context) }
                    )
                    SettingRowWithIcon(
                        icon = Icons.Outlined.Logout,
                        title = "Đăng xuất",
                        onClick = {
                            authViewModel.signout()
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

            // khoảng trống cuối danh sách
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // ===== Dialogs =====
        if (showAgeDialog) {
            NumberDialog(
                title = "Nhập tuổi",
                unit = "năm",
                initial = uiState.age?.toString(),
                onDismiss = { showAgeDialog = false },
                onSave = {
                    profileViewModel.updateAge(it.toIntOrNull())
                    showAgeDialog = false
                }
            )
        }

        if (showHeightDialog) {
            NumberDialog(
                title = "Nhập chiều cao",
                unit = if (uiState.heightUnit == HeightUnit.CM) "cm" else "inch",
                initial = uiState.heightCm?.toString(),
                onDismiss = { showHeightDialog = false },
                onSave = {
                    profileViewModel.updateHeight(it.toIntOrNull())
                    showHeightDialog = false
                }
            )
        }

        if (showWeightDialog) {
            NumberDialog(
                title = "Nhập trọng lượng",
                unit = if (uiState.weightUnit == WeightUnit.KG) "kg" else "lbs",
                initial = uiState.weightKg?.toString(),
                onDismiss = { showWeightDialog = false },
                onSave = {
                    profileViewModel.updateWeight(it.toIntOrNull())
                    showWeightDialog = false
                }
            )
        }

        if (showLanguageDialog) {
            LanguageDialog(
                selected = uiState.language,
                onDismiss = { showLanguageDialog = false },
                onSelect = {
                    profileViewModel.updateLanguage(it)
                    showLanguageDialog = false
                }
            )
        }
    }
}

// =============== Profile UI components ===============

@Composable
fun ProfileSectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 16.dp)
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}

@Composable
fun SettingRowWithIcon(
    icon: ImageVector,
    title: String,
    value: String? = null,
    onClick: (() -> Unit)? = null
) {
    val rowModifier = if (onClick != null) {
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    } else {
        Modifier.fillMaxWidth()
    }

    Row(
        modifier = rowModifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFF3E5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF7C4DFF),
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            color = Color(0xFF222222),
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        if (!value.isNullOrBlank()) {
            Text(
                text = value,
                color = Color(0xFF555555),
                fontSize = 13.sp,
                textAlign = TextAlign.End
            )
        }
    }
}

// =============== Dialogs ===============

@Composable
fun NumberDialog(
    title: String,
    unit: String,
    initial: String?,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(initial ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onSave(text) }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Giá trị ($unit)") },
                singleLine = true
            )
        }
    )
}

@Composable
fun LanguageDialog(
    selected: AppLanguage,
    onDismiss: () -> Unit,
    onSelect: (AppLanguage) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Chọn ngôn ngữ") },
        text = {
            Column {
                LanguageRow("Tiếng Việt", selected == AppLanguage.VI) {
                    onSelect(AppLanguage.VI)
                }
                Spacer(modifier = Modifier.height(6.dp))
                LanguageRow("English", selected == AppLanguage.EN) {
                    onSelect(AppLanguage.EN)
                }
            }
        }
    )
}

@Composable
fun LanguageRow(text: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) Color(0xFFEDE7FF) else Color.Transparent
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = Color(0xFF222222))
    }
}

// =============== Support ===============

private fun reportProblem(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf("support@gogym.com"))
        putExtra(Intent.EXTRA_SUBJECT, "Báo cáo vấn đề - Go Gym")
    }
    context.startActivity(intent)
}

private fun shareApp(context: Context) {
    val link = "https://drive.google.com/file/d/1iNDsZMU7c9Rty2_oAI3Y9beCEYYcdqvt/view?usp=sharing"
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "Hãy tập luyện cùng mình với Go Gym: $link")
    }
    context.startActivity(Intent.createChooser(intent, "Chia sẻ ứng dụng"))
}
