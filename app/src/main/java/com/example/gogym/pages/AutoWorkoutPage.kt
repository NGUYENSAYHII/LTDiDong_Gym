package com.example.gogym.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gogym.R

enum class AutoTarget(val label: String) {
    FULL_BODY("Toàn cơ thể"),
    ABS("Cơ bụng"),
    ARMS("Tay"),
    CHEST("Ngực"),
    LEGS("Chân"),
    BACK_SHOULDERS("Lưng và Vai")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoWorkoutPage(
    navController: NavController
) {
    var selectedTarget by remember { mutableStateOf(AutoTarget.FULL_BODY) }
    var selectedTotalMinutes by remember { mutableStateOf(15) }
    var selectedExerciseSeconds by remember { mutableStateOf(45) }
    var selectedRestSeconds by remember { mutableStateOf(30) }

    val primaryGreen = Color(0xFF00A000)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TẠO BÀI TẬP TỰ ĐỘNG",
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
                .verticalScroll(rememberScrollState())
        ) {
            // ----- MỤC TIÊU -----
            Text(
                text = "Mục tiêu",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            TargetGrid(
                selectedTarget = selectedTarget,
                onTargetSelected = { selectedTarget = it }
            )

            Spacer(Modifier.height(16.dp))

            // ----- Thời lượng tập luyện -----
            Text(
                text = "Thời lượng tập luyện",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            val totalOptions = listOf(5, 8, 10, 15, 20, 25, 30)
            FlowOptionRow(
                options = totalOptions,
                selected = selectedTotalMinutes,
                primaryColor = primaryGreen,
                labelBuilder = { "$it phút" },
                onSelectedChange = { selectedTotalMinutes = it }
            )

            Spacer(Modifier.height(16.dp))

            // ----- Thời lượng / Bài tập -----
            Text(
                text = "Thời lượng/Bài tập",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            val perExerciseOptions = listOf(30, 45, 60, 90)
            FlowOptionRow(
                options = perExerciseOptions,
                selected = selectedExerciseSeconds,
                primaryColor = primaryGreen,
                labelBuilder = { "$it giây" },
                onSelectedChange = { selectedExerciseSeconds = it }
            )

            Spacer(Modifier.height(16.dp))

            // ----- Thời gian nghỉ ngơi -----
            Text(
                text = "Thời gian nghỉ ngơi",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            val restOptions = listOf(15, 30, 45, 60, 90)
            FlowOptionRow(
                options = restOptions,
                selected = selectedRestSeconds,
                primaryColor = primaryGreen,
                labelBuilder = { "$it giây" },
                onSelectedChange = { selectedRestSeconds = it }
            )

            Spacer(Modifier.height(24.dp))

            // ----- Nút KHỞI TẠO -----
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .clickable {
                        val exerciseId = when (selectedTarget) {
                            AutoTarget.FULL_BODY -> "abs1"
                            AutoTarget.ABS -> "abs1"
                            AutoTarget.ARMS -> "arm1"
                            AutoTarget.CHEST -> "chest2"
                            AutoTarget.LEGS -> "leg3"
                            AutoTarget.BACK_SHOULDERS -> "back1"
                        }

                        val totalSeconds = selectedTotalMinutes * 60

                        // Thay startImmediately = true -> startImmediately = false
                        navigateToExerciseDetail(
                            navController,
                            exerciseId,
                            startImmediately = false,
                            seconds = totalSeconds
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "KHỞI TẠO",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "config",
                            tint = Color(0xFF0044AA)
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TargetGrid(
    selectedTarget: AutoTarget,
    onTargetSelected: (AutoTarget) -> Unit
) {
    val items = listOf(
        Triple(AutoTarget.FULL_BODY, "Toàn cơ thể", R.drawable.full_body),
        Triple(AutoTarget.ABS, "Cơ bụng", R.drawable.target_abs),
        Triple(AutoTarget.ARMS, "Tay", R.drawable.target_arms),
        Triple(AutoTarget.CHEST, "Ngực", R.drawable.nhom_conguc),
        Triple(AutoTarget.LEGS, "Chân", R.drawable.target_legs),
        Triple(AutoTarget.BACK_SHOULDERS, "Lưng và Vai", R.drawable.target_back_shoulders),
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = SpaceBetween
        ) {
            items.take(3).forEach { (target, label, imageRes) ->
                TargetItem(
                    target = target,
                    label = label,
                    imageRes = imageRes,
                    selected = selectedTarget == target,
                    onClick = { onTargetSelected(target) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = SpaceBetween
        ) {
            items.drop(3).forEach { (target, label, imageRes) ->
                TargetItem(
                    target = target,
                    label = label,
                    imageRes = imageRes,
                    selected = selectedTarget == target,
                    onClick = { onTargetSelected(target) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun TargetItem(
    target: AutoTarget,
    label: String,
    imageRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) Color(0xFF00A000) else Color(0xFFE0E0E0)
    val checkColor = Color(0xFF00A000)

    Column(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = label,
                modifier = Modifier.fillMaxSize()
            )

            if (selected) {
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(checkColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 13.sp
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T> FlowOptionRow(
    options: List<T>,
    selected: T,
    primaryColor: Color,
    labelBuilder: (T) -> String,
    onSelectedChange: (T) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp)
    ) {
        options.forEach { value ->
            val isSelected = value == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) primaryColor else Color.White)
                    .border(
                        width = 1.dp,
                        color = primaryColor,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelectedChange(value) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = labelBuilder(value),
                    fontSize = 13.sp,
                    color = if (isSelected) Color.White else Color.Black
                )
            }
        }
    }
}