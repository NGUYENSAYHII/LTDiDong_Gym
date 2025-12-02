package com.example.gogym.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutReportPage(
    navController: NavController
) {
    val today = LocalDate.now()
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }

    val daysOfWeek = remember {
        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "BÁO CÁO TẬP LUYỆN",
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
            Spacer(Modifier.height(8.dp))

            // Thanh chọn tháng năm
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    currentYearMonth = currentYearMonth.minusMonths(1)
                }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Previous month"
                    )
                }

                Text(
                    text = "${currentYearMonth.year} ${
                        currentYearMonth.month.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        )
                    }",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(onClick = {
                    currentYearMonth = currentYearMonth.plusMonths(1)
                }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next month"
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Hàng tên thứ Mon..Sun
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysOfWeek.forEach { dayName ->
                    Text(
                        text = dayName,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // Lưới các ngày trong tháng
            MonthGrid(
                yearMonth = currentYearMonth,
                today = today
            )

            Spacer(Modifier.height(24.dp))

        }
    }
}

@Composable
private fun MonthGrid(
    yearMonth: YearMonth,
    today: LocalDate
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    // ISO: Monday = 1, Sunday = 7
    val firstDayOfWeekIndex = (firstDayOfMonth.dayOfWeek.value % 7) // Mon=1,..Sun=7 -> 0..6
    val daysInMonth = yearMonth.lengthOfMonth()

    // Tổng ô = khoảng trống đầu tháng + số ngày
    val totalCells = firstDayOfWeekIndex + daysInMonth
    val weeks = (totalCells + 6) / 7 // làm tròn lên

    Column {
        var dayNumber = 1
        for (week in 0 until weeks) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (dow in 0 until 7) {
                    val cellIndex = week * 7 + dow
                    if (cellIndex < firstDayOfWeekIndex || dayNumber > daysInMonth) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp)
                        )
                    } else {
                        val date = yearMonth.atDay(dayNumber)
                        val isToday = date == today

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayNumber.toString(),
                                fontSize = 14.sp,
                                color = if (isToday) Color(0xFF00AA00) else Color.Black,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                        dayNumber++
                    }
                }
            }
        }
    }
}