package com.example.gogym.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPlanDetailPage(
    navController: NavController,
    onDayClick: (String) -> Unit = {}
) {
    val headerColor = Color(0xFFB0B0B0)   // xám trên mỗi ô
    val calendarRed = Color(0xFFFF4B4B)  // đỏ phần trên calendar

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KẾ HOẠCH TẬP LUYỆN TÙY CHỈNH",
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
            // 2 ô mỗi hàng cho 6 ngày đầu
            Row(modifier = Modifier.fillMaxWidth()) {
                DayCard(
                    labelVi = "Thứ Hai",
                    labelEn = "MON",
                    headerColor = headerColor,
                    calendarRed = calendarRed,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    onClick = { onDayClick("monday") }
                )
                DayCard(
                    labelVi = "Thứ Ba",
                    labelEn = "TUE",
                    headerColor = headerColor,
                    calendarRed = calendarRed,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    onClick = { onDayClick("tuesday") }
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                DayCard(
                    labelVi = "Thứ Tư",
                    labelEn = "WED",
                    headerColor = headerColor,
                    calendarRed = calendarRed,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    onClick = { onDayClick("wednesday") }
                )
                DayCard(
                    labelVi = "Thứ Năm",
                    labelEn = "THU",
                    headerColor = headerColor,
                    calendarRed = calendarRed,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    onClick = { onDayClick("thursday") }
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                DayCard(
                    labelVi = "Thứ Sáu",
                    labelEn = "FRI",
                    headerColor = headerColor,
                    calendarRed = calendarRed,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    onClick = { onDayClick("friday") }
                )
                DayCard(
                    labelVi = "Thứ Bảy",
                    labelEn = "SAT",
                    headerColor = headerColor,
                    calendarRed = calendarRed,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    onClick = { onDayClick("saturday") }
                )
            }

            // Hàng cuối: 1 ô Chủ Nhật ở giữa
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                DayCard(
                    labelVi = "Chủ Nhật",
                    labelEn = "SUN",
                    headerColor = headerColor,
                    calendarRed = calendarRed,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(4.dp),
                    onClick = { onDayClick("sunday") }
                )
            }
        }
    }
}

@Composable
private fun DayCard(
    labelVi: String,
    labelEn: String,
    headerColor: Color,
    calendarRed: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF7F7F7))
            .clickable { onClick() }
    ) {
        // Thanh xám bên trên
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerColor)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = labelVi,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(16.dp))

        // Icon lịch đơn giản (dùng Box + Text, sau có thể thay Image)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .aspectRatio(1f) // dạng vuông
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Phần đỏ trên
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.45f)
                        .clip(
                            RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp
                            )
                        )
                        .background(calendarRed)
                )

                // Phần trắng dưới + chữ MON/TUE...
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.55f)
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                        )
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = labelEn,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}