package com.example.gogym.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogym.R

@Composable
fun CustomizePage(
    onCustomPlanClick: () -> Unit,
    onOfficeWorkoutClick: () -> Unit,
    onAutoWorkoutClick: () -> Unit,
    onReportClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Tùy chỉnh",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        // Card 1
        CustomizeCard(
            imageRes = R.drawable.custom_plan,
            title = "KẾ HOẠCH TẬP LUYỆN TÙY CHỈNH",
            subtitle = "Tạo kế hoạch tập luyện của riêng bạn",
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp),
            onClick = onCustomPlanClick
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Báo cáo và Trình tạo bài tập",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Card 2
        CustomizeCard(
            imageRes = R.drawable.bai_tapvanphong,
            title = "BÀI TẬP VĂN PHÒNG",
            subtitle = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            onClick = onOfficeWorkoutClick
        )

        Spacer(Modifier.height(16.dp))

        // Card 3
        CustomizeCard(
            imageRes = R.drawable.auto_workout,
            title = "TẠO BÀI TẬP TỰ ĐỘNG",
            subtitle = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            onClick = onAutoWorkoutClick
        )

        Spacer(Modifier.height(16.dp))

        // Card 4
        CustomizeCard(
            imageRes = R.drawable.report_workout,
            title = "BÁO CÁO TẬP LUYỆN",
            subtitle = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            onClick = onReportClick
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun CustomizeCard(
    imageRes: Int,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xAA000000)
                        ),
                        startY = 200f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            if (subtitle.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color.White,
                    maxLines = 2
                )
            }
        }
    }
}