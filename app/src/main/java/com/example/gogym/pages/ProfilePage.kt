package com.example.gogym.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogym.AuthViewModel
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.gogym.R

data class ProfileRow(
    val section: String,
    val title: String,
    val value: String? = null,
    val iconRes: Int
)

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel) {

    val purple = Color(0xFFCF7BEA)
    val black = Color(0xFF000000)

    val rows = listOf(
        ProfileRow("Tôi", "Tuổi", "24", R.drawable.ic_profile),
        ProfileRow("Tôi", "Chiều cao", "180 cm", R.drawable.ic_profile),
        ProfileRow("Tôi", "Trọng lượng", "75KG", R.drawable.ic_profile),

        ProfileRow("Cài đặt", "Ngôn ngữ", "Tiếng Việt", R.drawable.ic_profile),
        ProfileRow("Cài đặt", "Lịch sử tập luyện", null, R.drawable.ic_profile),

        ProfileRow("Hỗ trợ", "Báo cáo vấn đề", null, R.drawable.ic_profile),
        ProfileRow("Hỗ trợ", "Chia sẻ ứng dụng", null, R.drawable.ic_profile),
        ProfileRow("Hỗ trợ", "Đăng xuất", null, R.drawable.ic_profile, ),
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(black)
    ) {
        item {
            // Header logo Go Gym
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Go Gym", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        var currentSection: String? = null

        items(rows) { row ->
            if (currentSection != row.section) {
                // section title
                Text(
                    text = row.section,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(black)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                currentSection = row.section
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(purple)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(row.iconRes),
                    contentDescription = row.title,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = row.title,
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )


                if (row.value != null) {
                    Text(
                        text = row.value,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                }
            }

            HorizontalDivider(color = black, thickness = 1.dp)
        }
    }
}
