package com.example.gogym.pages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gogym.HomeUiState
import com.example.gogym.HomeViewModel
import com.example.gogym.model.MealType


// HOME PAGE

@Composable
fun HomePage(
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    val uiState by homeViewModel.uiState
    var showEditGoalDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        homeViewModel.refreshTodayData()
    }

    // Dialog chỉnh kcal goal
    if (showEditGoalDialog) {
        EditGoalDialog(
            currentGoal = uiState.goalCalories,
            onDismiss = { showEditGoalDialog = false },
            onConfirm = { newGoal ->
                homeViewModel.updateGoalCalories(newGoal)
                showEditGoalDialog = false
            }
        )
    }

    Scaffold(containerColor = Color.Transparent) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFEAFBF2), Color(0xFFF7F9FF))
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // HEADER
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Go Gym",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = { /* search */ }) {
                            Icon(Icons.Default.Search, contentDescription = "Tìm Kiếm")
                        }
                        IconButton(onClick = { /* notification */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Thông báo")
                        }
                    }
                }

                // Dashboard Calories
                item {
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        tonalElevation = 6.dp,
                        color = Color.White
                    ) {
                        CalorieDashboard(
                            uiState = uiState,
                            onEditGoal = { showEditGoalDialog = true }
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }

                // Title
                item {
                    Text(
                        text = "Bữa ăn hàng ngày",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                }

                // Meal cards list
                val mealCards = listOf(
                    MealCardUi(
                        title = "Bữa sáng",
                        sub = "Đề xuất bữa ăn 547 kcal",
                        color = Color(0xFF4CAF50),
                        onAddClick = {
                            navController.navigate("add_meal/${MealType.BREAKFAST.name}")
                        }
                    ),
                    MealCardUi(
                        title = "Buổi Trưa",
                        sub = "Đề xuất bữa ăn 647 kcal",
                        color = Color(0xFFFFC857),
                        onAddClick = {
                            navController.navigate("add_meal/${MealType.LUNCH.name}")
                        }
                    ),
                    MealCardUi(
                        title = "Bữa Tối",
                        sub = "Đề xuất bữa ăn 500 kcal",
                        color = Color(0xFF4ECDC4),
                        onAddClick = {
                            navController.navigate("add_meal/${MealType.DINNER.name}")
                        }
                    ),
                    // WATER — có nút + và –
                    MealCardUi(
                        title = "Uống Nước",
                        sub = "Uống đủ 1.8L mỗi ngày",
                        extra = String.format("%.1f L today", uiState.waterLiter),
                        color = Color(0xFF4A90E2),
                        onAddClick = { homeViewModel.addWater() },
                        onMinusClick = { homeViewModel.removeWater() }
                    )
                )

                items(mealCards) { meal ->
                    DailyMealCard(
                        title = meal.title,
                        recommendedText = meal.sub,
                        extraLine = meal.extra,
                        color = meal.color,
                        onAddClick = meal.onAddClick,
                        onMinusClick = meal.onMinusClick
                    )
                    Spacer(Modifier.height(6.dp))
                }

                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}


// DATA HOLDER

private data class MealCardUi(
    val title: String,
    val sub: String,
    val extra: String? = null,
    val color: Color,
    val onAddClick: () -> Unit,
    val onMinusClick: (() -> Unit)? = null
)


// CALORIE DASHBOARD

@Composable
private fun CalorieDashboard(
    uiState: HomeUiState,
    onEditGoal: () -> Unit
) {
    val progress = (uiState.availableCalories.toFloat() / uiState.goalCalories.toFloat())
        .coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(220.dp)) {

            val strokeWidth = 18.dp.toPx()

            // background arc
            drawArc(
                startAngle = -210f,
                sweepAngle = 240f,
                useCenter = false,
                style = Stroke(width = strokeWidth),
                color = Color(0xFFE0E0E0)
            )

            // progress arc
            drawArc(
                startAngle = -210f,
                sweepAngle = 240f * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                color = Color(0xFF4CD964)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = uiState.availableCalories.toString(),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
            Text("kcal có sẵn", style = MaterialTheme.typography.bodySmall)
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp)
        ) {
            Text("Đã đốt", style = MaterialTheme.typography.bodySmall)
            Text(
                uiState.totalBurned.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text("Đã Ăn", style = MaterialTheme.typography.bodySmall)
            Text(
                uiState.totalEaten.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mục tiêu: ${uiState.goalCalories} kcal",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.width(6.dp))
            Icon(
                Icons.Default.Edit,
                contentDescription = "Chỉnh sửa mục tiêu",
                modifier = Modifier
                    .size(18.dp)
                    .clickable { onEditGoal() }
            )
        }
    }
}

// MEAL CARD

@Composable
private fun DailyMealCard(
    title: String,
    recommendedText: String,
    extraLine: String? = null,
    color: Color,
    onAddClick: () -> Unit,
    onMinusClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Dải màu trái
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = color,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = recommendedText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                if (extraLine != null) {
                    Text(
                        text = extraLine,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4A90E2)
                    )
                }
            }

            // Nút bấm trừ (nếu có)
            if (onMinusClick != null) {
                IconButton(onClick = onMinusClick) {
                    Icon(
                        Icons.Default.RemoveCircle,
                        contentDescription = "Remove",
                        tint = color
                    )
                }
            }

            IconButton(onClick = onAddClick) {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Thêm",
                    tint = color
                )
            }
        }
    }
}


// DIALOG SET GOAL

@Composable
private fun EditGoalDialog(
    currentGoal: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var text by remember { mutableStateOf(currentGoal.toString()) }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Đặt mục tiêu hàng ngày") },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        isError = it.toIntOrNull()?.let { g -> g <= 0 } ?: true
                    },
                    label = { Text("Mục tiêu kcal") },
                    singleLine = true,
                    isError = isError
                )
                if (isError) {
                    Text(
                        "Vui lòng nhập một số dương",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val value = text.toIntOrNull()
                if (value != null && value > 0) {
                    onConfirm(value)
                } else {
                    isError = true
                }
            }) {
                Text("Đồng ý")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
