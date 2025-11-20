// ExerciseDetailPage.kt
package com.example.gogym.pages

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gogym.R
import kotlinx.coroutines.delay
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.content.Context //thêm

data class ExerciseDetail(
    val id: String,
    val name: String,
    val difficulty: String,
    val timeOrReps: String,        // ví dụ: "30 giây" hoặc "3x12"
    val description: String,
    val fullImageRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailPage(
    exerciseId: String,
    navController: NavHostController
) {
    val context = LocalContext.current

    // Hard-code dữ liệu chi tiết (sau này có thể mở rộng thành map lớn)
    val detail = when (exerciseId) {
        "abs1" -> ExerciseDetail(exerciseId, "Chéo", "Dễ", "30 giây", "Nằm ngửa, tay đặt sau đầu. Nâng vai lên và chéo tay chạm đầu gối đối diện luân phiên.", R.drawable.abs_cheo_full ?: R.drawable.ic_profile)
        "abs2" -> ExerciseDetail(exerciseId, "Đẩy qua", "Dễ", "3x15 mỗi bên", "Nằm ngửa, nâng chân lên 90 độ, tay duỗi thẳng đẩy qua chạm gót chân luân phiên.", R.drawable.abs_dayqua_full ?: R.drawable.ic_profile)
        "abs3" -> ExerciseDetail(exerciseId, "Gập truyền thống", "Dễ", "3x20", "Nằm ngửa, gập người lên chạm tay vào đầu gối.", R.drawable.abs_dayqua_full ?: R.drawable.ic_profile)
        "arm1" -> ExerciseDetail(exerciseId, "Ở cửa", "Dễ", "3x12", "Đứng ở khung cửa, chống tay vào hai bên khung và hạ người xuống từ từ.", R.drawable.arm_cua_full ?: R.drawable.ic_profile)
        "chest1" -> ExerciseDetail(exerciseId, "Phối cung thủ", "Trung bình", "3x10 mỗi bên", "Chống đẩy bình thường nhưng dồn trọng lượng sang một bên như bắn cung.", R.drawable.workout_default ?: R.drawable.ic_profile)
        else -> ExerciseDetail(exerciseId, "Bài tập siêu hay", "Trung bình", "30 giây", "Đây là một bài tập tuyệt vời cho bạn. Hãy cố lên nhé!", R.drawable.workout_default ?: R.drawable.ic_profile)
    }

    var showTimer by remember { mutableStateOf(false) }

    // Full màn hình Timer
    if (showTimer) {
        FullScreenTimer(
            initialSeconds = 30,
            onFinish = { showTimer = false },
            onBack = { showTimer = false }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Chi tiết bài tập") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(R.drawable.ic_home ?: R.drawable.ic_profile), contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
        ) {
            // Ảnh lớn
            Image(
                painter = painterResource(detail.fullImageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(detail.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("${detail.difficulty} • ${detail.timeOrReps}", fontSize = 16.sp, color = Color.Gray)

                Spacer(Modifier.height(24.dp))

                Text("Cách thực hiện", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = detail.description,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.DarkGray
                )

                Spacer(Modifier.height(40.dp))

                Button(
                    onClick = { showTimer = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B4B))
                ) {
                    Text("BẮT ĐẦU TẬP", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ==================== FULL SCREEN TIMER ĐẸP LẮM ====================
@Composable
fun FullScreenTimer(
    initialSeconds: Int = 30,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var secondsLeft by remember { mutableStateOf(initialSeconds) }
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(isRunning) {
        if (isRunning && secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
            if (secondsLeft == 0) {
                // Âm thanh + rung khi hết giờ
                try {
                    val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    val r = RingtoneManager.getRingtone(context, ringtone)
                    r.play()
                } catch (_: Exception) {}
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(800, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator.vibrate(800)
                }
                onFinish()
            }
        }
    }

    BackHandler { onBack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$secondsLeft",
                fontSize = if (secondsLeft <= 5) 120.sp else 100.sp,
                fontWeight = FontWeight.Bold,
                color = if (secondsLeft <= 5) Color(0xFFFF4B4B) else Color.White
            )

            Spacer(Modifier.height(40.dp))

            AnimatedVisibility(visible = secondsLeft <= 0) {
                Text("HOÀN THÀNH! 💪", fontSize = 32.sp, color = Color(0xFF4CAF50))
            }

            Spacer(Modifier.height(80.dp))

            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("DỪNG LẠI", color = Color.Black, fontSize = 18.sp)
            }
        }
    }
}