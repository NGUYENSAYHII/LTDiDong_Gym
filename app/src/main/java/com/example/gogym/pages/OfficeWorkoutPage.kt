package com.example.gogym.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gogym.R

// Thời lượng: 5, 10, 15 phút
enum class OfficeDuration(val label: String) {
    FIVE("5 phút"),
    TEN("10 phút"),
    FIFTEEN("15 phút")
}

// Model 1 bài tập
data class OfficeExercise(
    val id: String,
    val title: String,
    val target: String,
    val durationSeconds: Int,
    val imageRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfficeWorkoutPage(
    navController: NavController,
    // thêm optional rootNavController để điều hướng đến route đăng ký ở NavHost gốc nếu cần
    rootNavController: NavController? = null
) {
    var selectedDuration by remember { mutableStateOf(OfficeDuration.FIVE) }

    // Dummy data cho từng tab – sau này bạn có thể lấy từ CSDL
    val exercises = remember(selectedDuration) {
        when (selectedDuration) {
            OfficeDuration.FIVE -> sampleFiveMinutesExercises()
            OfficeDuration.TEN -> sampleTenMinutesExercises()
            OfficeDuration.FIFTEEN -> sampleFifteenMinutesExercises()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bài tập văn phòng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Thanh chọn 5 / 10 / 15 phút
            DurationTabs(
                selected = selectedDuration,
                onSelectedChange = { selectedDuration = it }
            )

            Spacer(Modifier.height(12.dp))

            // Danh sách bài tập
            Column(
                modifier = Modifier
                    .weight(1f, fill = true)
                    .fillMaxWidth()
            ) {
                exercises.forEach { ex ->
                    // pass navController và rootNavController để item có thể điều hướng an toàn
                    OfficeExerciseItem(exercise = ex, navController = navController, rootNavController = rootNavController)
                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(8.dp))

            // Nút BẮT ĐẦU
            StartButton(
                onClick = {
                    // TODO: điều hướng sang màn thực hiện bài tập theo list exercises
                    // Hiện tạm: nếu muốn chạy toàn bộ list, bạn có thể điều hướng đến 1 màn workout runner
                }
            )
        }
    }
}

@Composable
private fun DurationTabs(
    selected: OfficeDuration,
    onSelectedChange: (OfficeDuration) -> Unit
) {
    val activeColor = Color(0xFF00A000) // xanh lá
    val borderColor = Color(0xFF008000)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        DurationTabItem(
            duration = OfficeDuration.FIVE,
            selected = selected == OfficeDuration.FIVE,
            activeColor = activeColor,
            onSelectedChange = onSelectedChange,
            modifier = Modifier.weight(1f)
        )
        DurationTabItem(
            duration = OfficeDuration.TEN,
            selected = selected == OfficeDuration.TEN,
            activeColor = activeColor,
            onSelectedChange = onSelectedChange,
            modifier = Modifier.weight(1f)
        )
        DurationTabItem(
            duration = OfficeDuration.FIFTEEN,
            selected = selected == OfficeDuration.FIFTEEN,
            activeColor = activeColor,
            onSelectedChange = onSelectedChange,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DurationTabItem(
    duration: OfficeDuration,
    selected: Boolean,
    activeColor: Color,
    onSelectedChange: (OfficeDuration) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(if (selected) activeColor else Color.White)
            .clickable { onSelectedChange(duration) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = duration.label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) Color.White else Color.Black
        )
    }
}

@Composable
private fun OfficeExerciseItem(
    exercise: OfficeExercise,
    navController: NavController,
    // optional root controller — nếu caller truyền vào, ta sẽ dùng nó để điều hướng lên NavHost gốc
    rootNavController: NavController? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable {
                // Chọn controller phù hợp: ưu tiên rootNavController nếu có, nếu không thì dùng navController hiện tại.
                val controllerToUse = rootNavController ?: navController
                // Use helper to always produce the correct route (3 params).
                // seconds = exercise.durationSeconds so detail page will show that duration when started.
                navigateToExerciseDetail(controllerToUse, exercise.id, startImmediately = false, seconds = exercise.durationSeconds)
            }
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = exercise.imageRes),
            contentDescription = exercise.title,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = exercise.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Mục tiêu: ${exercise.target}",
                fontSize = 13.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "${exercise.durationSeconds} giây",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun StartButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "BẮT ĐẦU",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Dữ liệu mẫu – thay imageRes bằng hình bạn có, ví dụ:
 * R.drawable.office_ex1, office_ex2,...
 *
 * IMPORTANT: ids here must match ids handled by ExerciseDetailPage mapping (e.g. "off1","off2","off3","off4").
 */

private fun sampleFiveMinutesExercises(): List<OfficeExercise> = listOf(
    OfficeExercise(
        id = "off1",
        title = "Gập bụng đạp xe",
        target = "Toàn cơ bụng",
        durationSeconds = 30,
        imageRes = R.drawable.office_workout_gapbung // đổi thành tên hình thực tế
    ),
    OfficeExercise(
        id = "off2",
        title = "Tấm ván bên",
        target = "Thân trên",
        durationSeconds = 30,
        imageRes = R.drawable.office_workout_tamvanben
    ),
    OfficeExercise(
        id = "off3",
        title = "Đứng V",
        target = "Thân dưới",
        durationSeconds = 30,
        imageRes = R.drawable.office_workout_dungv
    ),
    OfficeExercise(
        id = "off4",
        title = "Ghế rung",
        target = "Cơ bụng dưới",
        durationSeconds = 30,
        imageRes = R.drawable.office_workout_gherung
    )
)

private fun sampleTenMinutesExercises(): List<OfficeExercise> = listOf(
    OfficeExercise(
        id = "off1",
        title = "Gập bụng đạp xe",
        target = "Toàn cơ bụng",
        durationSeconds = 30,
        imageRes = R.drawable.office_workout_gapbung
    ),
    OfficeExercise(
        id = "off2",
        title = "Ghế gập bụng",
        target = "Toàn cơ bụng",
        durationSeconds = 30,
        imageRes = R.drawable.office_workout_ghegapbung
    ),
    OfficeExercise(
        id = "off3",
        title = "Vòng tròn bên",
        target = "Thân trên",
        durationSeconds = 30,
        imageRes = R.drawable.office_workout_vtb
    ),
    OfficeExercise(
        id = "off4",
        title = "Tường chống đẩy một tay",
        target = "Thân trên",
        durationSeconds = 30,
        imageRes = R.drawable.office_workout_chongday1tay
    )
)

private fun sampleFifteenMinutesExercises(): List<OfficeExercise> = listOf(
    OfficeExercise(
        id = "off1",
        title = "Tấm ván một tay",
        target = "Thân trên",
        durationSeconds = 30,
        imageRes = R.drawable.office_workout_chongday1tay
    ),
    OfficeExercise(
        id = "off2",
        title = "Ngồi xổm bằng ghế",
        target = "Thân dưới",
        durationSeconds = 30,
        imageRes = R.drawable.office_workout_ngoi
    ),
    OfficeExercise(
        id = "off3",
        title = "Ghế gập bụng",
        target = "Toàn cơ bụng",
        durationSeconds = 30,
        imageRes = R.drawable.office_workout_ghegapbung
    ),
    OfficeExercise(
        id = "off4",
        title = "Nâng chân",
        target = "Toàn cơ bụng",
        durationSeconds = 30,
        imageRes = R.drawable.office_workout_nangchan
    )
)