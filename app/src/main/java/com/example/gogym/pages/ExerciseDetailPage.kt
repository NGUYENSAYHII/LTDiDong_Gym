package com.example.gogym.pages

import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.gogym.R
import kotlinx.coroutines.delay

// Dữ liệu chi tiết + calo đốt cháy mỗi bài (ước lượng MET × thời gian × cân nặng 70kg)
data class ExerciseDetail(
    val id: String,
    val name: String,
    val difficulty: String,
    val timeOrReps: String,
    val description: String,
    val fullImageRes: Int,
    val caloriesPerMinute: Double = 8.0   // trung bình 8 kcal/phút cho các bài bodyweight
)

// Hàm tiện ích tính calo (có thể mở rộng sau)
private fun calculateCalories(minutes: Int, caloriesPerMinute: Double = 8.0): Int =
    (minutes * caloriesPerMinute).toInt()

// Lookup function để đảm bảo tên + ảnh + mô tả đồng bộ giữa các màn
fun lookupExerciseDetail(exerciseId: String): ExerciseDetail {
    return when (exerciseId) {
        // ==================== CƠ BỤNG ====================
        "abs1" -> ExerciseDetail(
            id = exerciseId,
            name = "Chéo",
            difficulty = "Dễ",
            timeOrReps = "30 giây",
            description = "Nằm ngửa, tay sau đầu. Nâng vai lên và chéo tay chạm đầu gối đối diện luân phiên.",
            fullImageRes = R.drawable.bung_cheo,
            caloriesPerMinute = 7.5
        )
        "abs2" -> ExerciseDetail(exerciseId, "Đẩy qua", "Dễ", "30 giây", "Nằm ngửa, nâng chân 90°, tay duỗi thẳng đẩy qua chạm gót luân phiên.", R.drawable.b_dayqua, 8.0)
        "abs3" -> ExerciseDetail(exerciseId, "Gập truyền thống", "Dễ", "3x20", "Nằm ngửa, gập người lên chạm tay vào đầu gối.", R.drawable.b_truyenthong,7.8)
        "abs4" -> ExerciseDetail(exerciseId, "Chạm gót", "Dễ", "30 giây", "Nằm ngửa, tay duỗi thẳng chạm gót chân luân phiên.", R.drawable.b_chamgot,7.6)

        // ==================== TAY ====================
        "arm1" -> ExerciseDetail(exerciseId, "Ở cửa", "Dễ", "3x12", "Đứng ở cửa, dùng tay chống đẩy vào khung cửa.", R.drawable.t_ocua,8.5)
        "arm2" -> ExerciseDetail(exerciseId, "Tay đẩy ghế", "Dễ", "3x15", "Dùng ghế làm điểm tựa, chống đẩy tay.", R.drawable.t_dayghe,9.0)
        "arm3" -> ExerciseDetail(exerciseId, "Căng ngực nâng động", "Dễ", "30 giây", "Căng ngực rồi nâng tay lên cao.", R.drawable.t_cangnguc,7.2)
        "arm4" -> ExerciseDetail(exerciseId, "Phối cung thủ", "Trung bình", "3x12 mỗi bên", "Giả vờ kéo cung, siết cơ tay.", R.drawable.t_phoicungthu,9.5)

        // ==================== NGỰC ====================
        "chest1" -> ExerciseDetail(exerciseId, "Phối cung thủ", "Trung bình", "3x12", "Tương tự bài tay nhưng tập trung vào ngực.", R.drawable.t_phoicungthu,9.5)
        "chest2" -> ExerciseDetail(exerciseId, "Nâng cao cánh tay", "Trung bình", "30 giây", "Nâng tay lên cao, siết cơ ngực.", R.drawable.n_caotay,8.0)
        "chest3" -> ExerciseDetail(exerciseId, "Máy khoan đá", "Dễ", "30 giây", "Chạy tại chỗ nâng cao gối.", R.drawable.n_khoanda,10.0)
        "chest4" -> ExerciseDetail(exerciseId, "Mở rộng cơ tam đầu", "Khó", "3x10", "Chống đẩy hẹp tay.", R.drawable.n_cotamdau,11.0)

        // ==================== CHÂN ====================
        "leg1" -> ExerciseDetail(exerciseId, "Ở cửa", "Dễ", "3x12 mỗi chân", "Dùng khung cửa hỗ trợ squat một chân.", R.drawable.t_ocua,9.0)
        "leg2" -> ExerciseDetail(exerciseId, "Chống đẩy bằng ngón chân", "Trung bình", "30 giây", "Plank trên ngón chân.", R.drawable.n_ngonchan,10.5)
        "leg3" -> ExerciseDetail(exerciseId, "Nâng cao cánh tay", "Trung bình", "30 giây", "Squat + nâng tay.", R.drawable.n_caotay,9.8)
        "leg4" -> ExerciseDetail(exerciseId, "Con sâu", "Dễ", "30 giây", "Inchworm – đi bộ tay.", R.drawable.c_consau,9.2)

        // ==================== LƯNG & VAI ====================
        "back1" -> ExerciseDetail(exerciseId, "Giới hạn chân thẳng", "Dễ", "30 giây", "Cúi người giữ chân thẳng.", R.drawable.v_chanthang,7.5)
        "back2" -> ExerciseDetail(exerciseId, "Squat và nhảy", "Trung bình", "3x10", "Squat rồi bật nhảy.", R.drawable.v_squatnhay,12.0)
        "back3" -> ExerciseDetail(exerciseId, "Trượt băng ngồi xổm", "Trung bình", "30 giây", "Side skater.", R.drawable.v_ngoixom,10.8)
        "back4" -> ExerciseDetail(exerciseId, "Tư thế trẻ em", "Dễ", "60 giây", "Thư giãn lưng.", R.drawable.v_treem,5.0)

        // ==================== VĂN PHÒNG ====================
        // Các drawable office_* phải có trong res/drawable (png/jpg/webp hoặc vector)
        "off1" -> ExerciseDetail(exerciseId, "Gập bụng đạp xe", "Dễ", "30 giây", "Bicycle crunch tại chỗ.", R.drawable.office_workout_gapbung, 6.5)
        "off2" -> ExerciseDetail(exerciseId, "Tấm ván bên", "Dễ", "30 giây", "Tấm ván bên giữ thăng bằng.", R.drawable.office_workout_tamvanben, 6.8)
        "off3" -> ExerciseDetail(exerciseId, "Đứng V", "Dễ", "30 giây", "Đứng V tập thân dưới.", R.drawable.office_workout_dungv, 6.2)
        "off4" -> ExerciseDetail(exerciseId, "Ghế rung", "Dễ", "30 giây", "Động tác rung trên ghế tập cơ bụng dưới.", R.drawable.office_workout_gherung, 6.0)

        // ==================== KICKBOXING ====================
        "kb1" -> ExerciseDetail(exerciseId, "Tay đẩy ghế", "Dễ", "3x15", "Đấm nhanh vào ghế.", R.drawable.t_dayghe,11.0)
        "kb2" -> ExerciseDetail(exerciseId, "Chống đẩy nâng thân người", "Dễ", "3x10", "Burpee không nhảy.", R.drawable.k_hitdat,12.5)
        "kb3" -> ExerciseDetail(exerciseId, "Giới hạn chân thẳng", "Dễ", "30 giây", "Đá cao giữ chân thẳng.", R.drawable.v_chanthang,10.0)
        "kb4" -> ExerciseDetail(exerciseId, "Ngồi trên không", "Dễ", "30 giây", "Wall sit + đấm liên tục.", R.drawable.k_hitdat,13.0)

        else -> ExerciseDetail(
            id = "unknown",
            name = "Không tìm thấy bài tập",
            difficulty = "—",
            timeOrReps = "—",
            description = "ID bài tập không hợp lệ.",
            fullImageRes = android.R.drawable.ic_menu_gallery,
            caloriesPerMinute = 0.0
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailPage(
    exerciseId: String,
    navController: NavHostController,
    startImmediately: Boolean = false,
    initialSeconds: Int? = null
) {
    val context = LocalContext.current

    // Lấy detail qua helper để đảm bảo đồng bộ tên và ảnh giữa các trang
    val detail = lookupExerciseDetail(exerciseId)

    // parse seconds từ detail.timeOrReps (ví dụ "30 giây", "1 phút")
    fun parseSecondsFromTimeOrReps(text: String): Int {
        val lower = text.lowercase()
        val regex = Regex("(\\d+)")
        val match = regex.find(lower)
        val num = match?.groupValues?.get(1)?.toIntOrNull() ?: return 30
        return when {
            lower.contains("giây") -> num
            lower.contains("phút") || lower.contains("min") -> num * 60
            else -> if (num >= 60) num else 30
        }
    }

    // If an initialSeconds override is supplied, use it; otherwise parse from detail.timeOrReps.
    val defaultSeconds = initialSeconds ?: parseSecondsFromTimeOrReps(detail.timeOrReps)

    // show a user-friendly string for the displayed time (use selected override if present)
    val displayTimeOrReps = initialSeconds?.let { secs ->
        if (secs % 60 == 0) "${secs / 60} phút" else "$secs giây"
    } ?: detail.timeOrReps

    var showTimer by remember { mutableStateOf(startImmediately) }
    var burnedCalories by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header + nút quay lại
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.Black
                )
            }
            Spacer(Modifier.width(8.dp))
            Text("Chi tiết bài tập", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        Text(detail.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Cấp độ: ${detail.difficulty}", fontSize = 18.sp, color = Color.Gray)
        Text(displayTimeOrReps, fontSize = 20.sp, color = Color(0xFFFF4B4B))

        Spacer(Modifier.height(20.dp))

        // Ảnh lớn – an toàn bằng Coil
        val imagePainter = rememberAsyncImagePainter(
            model = detail.fullImageRes,
            error = painterResource(android.R.drawable.ic_menu_gallery),
            onError = { Log.e("ExerciseDetail", "Load image error: ${it.result.throwable}") }
        )
        Image(
            painter = imagePainter,
            contentDescription = detail.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(24.dp))

        Text("Hướng dẫn", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Text(detail.description, fontSize = 16.sp, lineHeight = 24.sp)

        Spacer(Modifier.height(40.dp))

        // Hiển thị calo đã đốt (nếu đã tập xong)
        if (burnedCalories > 0) {
            Text(
                text = "Đã đốt: $burnedCalories kcal",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )
            Spacer(Modifier.height(16.dp))
        }

        Button(
            onClick = { showTimer = true },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp)
        ) {
            Text("BẮT ĐẦU TẬP", fontSize = 18.sp)
        }
    }

    // Fullscreen Timer
    if (showTimer) {
        FullScreenTimer(
            seconds = defaultSeconds,
            caloriesPerMinute = detail.caloriesPerMinute,
            onFinish = {
                val minutes = kotlin.math.max(1, (defaultSeconds + 59) / 60)
                burnedCalories = calculateCalories(minutes, detail.caloriesPerMinute)
                showTimer = false
            },
            onBack = { showTimer = false }
        )
    }

    // Quay lại bằng nút back cứng của Android
    BackHandler(enabled = showTimer) {
        showTimer = false
    }
}

// Timer fullscreen + tính calo (sửa UI: nền trắng, text đen, thêm "Đang hẹn giờ", nút đen)
@Composable
fun FullScreenTimer(
    seconds: Int = 30,
    caloriesPerMinute: Double,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var timeLeft by remember { mutableIntStateOf(seconds) }

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        } else if (timeLeft == 0) {
            // Âm thanh + rung
            try {
                RingtoneManager.getRingtone(
                    context,
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                )?.play()
            } catch (e: Exception) { Log.e("Timer", "Ringtone error", e) }

            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
            } else @Suppress("DEPRECATION") v.vibrate(1000)

            onFinish()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),  // Nền trắng
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Đang hẹn giờ",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black  // Màu đen dễ thấy
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "$timeLeft",
                fontSize = if (timeLeft <= 5) 140.sp else 120.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black  // Màu đen
            )
            Spacer(Modifier.height(60.dp))
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),  // Nút màu đen
                modifier = Modifier
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text("DỪNG LẠI", color = Color.White, fontSize = 20.sp)  // Text trắng dễ thấy
            }
        }
    }
}