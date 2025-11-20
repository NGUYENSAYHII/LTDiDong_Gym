// WorkoutPage.kt
package com.example.gogym.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gogym.R

data class WorkoutCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val thumbRes: Int
)

@Composable
fun WorkoutPage(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        WorkoutCategory("abs", "Bài tập cơ bụng", "Bài tập cơ bụng cho 6 múi", R.drawable.arm_thumb ?: R.drawable.ic_profile),
        WorkoutCategory("arms", "Bài tập tay", "Các bài tập tay tốt nhất tại nhà", R.drawable.arm_thumb ?: R.drawable.ic_profile),
        WorkoutCategory("chest", "Bài tập ngực", "Xây dựng bộ ngực lớn hơn", R.drawable.leg_thumb ?: R.drawable.ic_profile),
        WorkoutCategory("legs", "Bài tập chân", "Thêm sức mạnh và nhanh hơn", R.drawable.leg_thumb ?: R.drawable.ic_profile),
        WorkoutCategory("back", "Lưng & Vai", "Bộ vai rộng và lưng khoẻ", R.drawable.back_thumb ?: R.drawable.ic_profile),
        WorkoutCategory("office", "Bài tập văn phòng", "Tập thể dục tại bàn làm việc của bạn", R.drawable.office_thumb ?: R.drawable.ic_profile),
        WorkoutCategory("kickboxing", "Tập luyện Kickboxing", "Kết hợp cardio và sức mạnh", R.drawable.kickboxing_thumb ?: R.drawable.ic_profile),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        items(categories) { category ->
            Card(
                onClick = { navController.navigate("workout_detail/${category.id}") },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    Image(
                        painter = painterResource(id = category.thumbRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    )
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = category.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = category.subtitle, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}