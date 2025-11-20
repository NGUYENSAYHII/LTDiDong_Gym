// WorkoutCategoryDetailPage.kt
package com.example.gogym.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gogym.R
import androidx.compose.ui.draw.clip   // THÊM DÒNG import clip

data class Exercise(
    val id: String,
    val name: String,
    val difficulty: String,
    val thumbRes: Int,
    val isPremium: Boolean = false
)

@Composable
fun WorkoutCategoryDetailPage(
    categoryId: String,
    navController: NavHostController
) {
    val title = when(categoryId) {
        "abs" -> "Bài tập cơ bụng"
        "arms" -> "Bài tập tay"
        "chest" -> "Bài tập ngực"
        "legs" -> "Bài tập chân"
        "back" -> "Lưng & Vai"
        "office" -> "Bài tập văn phòng"
        "kickboxing" -> "Tập luyện Kickboxing"
        else -> "Bài tập"
    }

    val exercises = when(categoryId) {
        "abs" -> listOf(  // Cơ bụng
            Exercise("abs1", "Chéo", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("abs2", "Đẩy qua", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("abs3", "Gập truyền thống", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("abs4", "Chạm gót", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("abs5", "Gập đầu gối", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("abs6", "Plank", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("abs7", "Leo núi", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("abs8", "Russian twist", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("abs9", "V-up", "Khó", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("abs10", "Flutter kicks", "Khó", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
        )
        "arms" -> listOf(  // Tay
            Exercise("arm1", "Ở cửa", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("arm2", "Tay đẩy ghế", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("arm3", "Căng ngực nâng động", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("arm4", "Phối cung thủ", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("arm5", "Nâng cao cánh tay", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("arm6", "Máy khoan đá", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("arm7", "Chống đẩy kim cương", "Khó", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("arm8", "Pike push-up", "Khó", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
        )
        "chest" -> listOf(  // Ngực
            Exercise("chest1", "Phối cung thủ", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("chest2", "Nâng cao cánh tay", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("chest3", "Máy khoan đá", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("chest4", "Chống đẩy kim cương", "Khó", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("chest5", "Chống đẩy rộng", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("chest6", "Chống đẩy hẹp", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("chest7", "Hindu push-up", "Khó", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("chest8", "Archer push-up", "Khó", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
        )
        "legs" -> listOf(  // Chân
            Exercise("leg1", "Ở cửa", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("leg2", "Chống đẩy bằng ngón chân", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("leg3", "Nâng cao cánh tay", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("leg4", "Con sâu", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("leg5", "Squat", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("leg6", "Lunges", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("leg7", "Pistol squat", "Khó", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("leg8", "Jump squat", "Khó", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
        )
        "back" -> listOf(  // Lưng & Vai
            Exercise("back1", "Giới hạn chân thẳng", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("back2", "Squat và nhảy", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("back3", "Trượt băng ngồi xổm", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("back4", "Tư thế trẻ em", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("back5", "Superman", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("back6", "Cobra stretch", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("back7", "Bird dog", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile),
            Exercise("back8", "Wall angel", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile),
        )
        "office" -> listOf(  // Văn phòng (hầu hết premium)
            Exercise("off1", "Ghế chéo", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("off2", "Ghế gập bụng", "Khó", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("off3", "Gập bụng đạp xe", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("off4", "Đạp xe", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("off5", "Nâng đầu gối đôi", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
        )
        "kickboxing" -> listOf(  // Kickboxing (toàn bộ premium)
            Exercise("kb1", "Tay đẩy ghế", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("kb2", "Chống đẩy nâng thân người", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("kb3", "Giới hạn chân thẳng", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("kb4", "Ngồi trên không", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("kb5", "Đấm nhanh - Jab", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("kb6", "Cross punch", "Dễ", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("kb7", "Hook", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
            Exercise("kb8", "Uppercut", "Trung bình", R.drawable.all_in ?: R.drawable.ic_profile, isPremium = true),
        )
        else -> emptyList()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(painter = painterResource(R.drawable.ic_home ?: R.drawable.ic_profile), contentDescription = "Back")
                }
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            HorizontalDivider()
        }

        items(exercises) { ex ->
            ExerciseItem(ex) {
                navController.navigate("exercise_detail/${ex.id}")
            }
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
        }

        if (exercises.any { it.isPremium }) {
            item {
                PremiumBanner()
            }
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(exercise.thumbRes),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(exercise.name, fontWeight = FontWeight.Medium, fontSize = 16.sp)
            Text("Cấp độ: ${exercise.difficulty}", fontSize = 13.sp, color = Color.Gray)
        }
        if (exercise.isPremium) {
            Icon(painter = painterResource(R.drawable.ic_profile), "Premium", tint = Color(0xFFFFD700))
        }
    }
}

@Composable
fun PremiumBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFFFF8E1), RoundedCornerShape(12.dp))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("MỞ KHÓA TẤT CẢ", fontWeight = FontWeight.Bold, color = Color(0xFFFF8F00))
    }
}