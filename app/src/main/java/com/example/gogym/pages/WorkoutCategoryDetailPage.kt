// WorkoutCategoryDetailPage.kt
package com.example.gogym.pages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import coil.compose.rememberAsyncImagePainter  // Import cho Coil

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
            Exercise("abs1", "Chéo", "Dễ", R.drawable.bung_cheo),
            Exercise("abs2", "Đẩy qua", "Dễ", R.drawable.b_dayqua),
            Exercise("abs3", "Gập truyền thống", "Dễ", R.drawable.b_truyenthong),
            Exercise("abs4", "Chạm gót", "Dễ", R.drawable.b_chamgot),
        )

        "arms" -> listOf(  // Tay
            Exercise("arm1", "Ở cửa", "Dễ", R.drawable.t_ocua),
            Exercise("arm2", "Tay đẩy ghế", "Dễ", R.drawable.t_dayghe),
            Exercise("arm3", "Căng ngực nâng động", "Dễ", R.drawable.t_cangnguc),
            Exercise("arm4", "Phối cung thủ", "Trung bình", R.drawable.t_phoicungthu),
        )

        "chest" -> listOf(  // Ngực
            Exercise("chest1", "Phối cung thủ", "Trung bình", R.drawable.t_phoicungthu),
            Exercise("chest2", "Nâng cao cánh tay", "Trung bình", R.drawable.n_caotay),
            Exercise("chest3", "Máy khoan đá", "Dễ", R.drawable.n_khoanda),
            Exercise("chest4", "Mở rộng cơ tam đầu", "Khó", R.drawable.n_cotamdau),
        )

        "legs" -> listOf(  // Chân
            Exercise("leg1", "Ở cửa", "Dễ", R.drawable.t_ocua),
            Exercise("leg2", "Chống đẩy bằng ngón chân", "Trung bình", R.drawable.n_ngonchan),
            Exercise("leg3", "Nâng cao cánh tay", "Trung bình", R.drawable.n_caotay),
            Exercise("leg4", "Con sâu", "Dễ", R.drawable.c_consau),
        )

        "back" -> listOf(  // Lưng & Vai
            Exercise("back1", "Giới hạn chân thẳng", "Dễ", R.drawable.v_chanthang),
            Exercise("back2", "Squat và nhảy", "Trung bình", R.drawable.v_squatnhay),
            Exercise("back3", "Trượt băng ngồi xổm", "Trung bình", R.drawable.v_ngoixom),
            Exercise("back4", "Tư thế trẻ em", "Dễ", R.drawable.v_treem),
        )

        "office" -> listOf(  // Văn phòng (hầu hết premium)
            Exercise("off1", "Ghế chéo", "Dễ", R.drawable.t_ocua),
            Exercise("off2", "Ghế gập bụng", "Khó", R.drawable.t_phoicungthu),
            Exercise("off3", "Gập bụng đạp xe", "Trung bình", R.drawable.t_dayghe),
            Exercise("off4", "Đạp xe", "Trung bình", R.drawable.t_cangnguc),
        )

        "kickboxing" -> listOf(  // Kickboxing (toàn bộ premium)
            Exercise("kb1", "Tay đẩy ghế", "Dễ", R.drawable.t_dayghe),
            Exercise("kb2", "Chống đẩy nâng thân người", "Dễ", R.drawable.k_hitdat),
            Exercise("kb3", "Giới hạn chân thẳng", "Dễ", R.drawable.v_chanthang),
            Exercise("kb4", "Ngồi trên không", "Dễ", R.drawable.k_hitdat),
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
                    // Sử dụng Coil để load an toàn với fallback
                    val backIconPainter = rememberAsyncImagePainter(
                        model = R.drawable.baseline_arrow_back_24,
                        error = painterResource(android.R.drawable.ic_menu_gallery),  // Fallback nếu error
                        onError = { Log.e("WorkoutCategoryDetail", "Error loading ic_home: ${it.result.throwable}") }
                    )
                    Icon(painter = backIconPainter, contentDescription = "Back")
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
        // Sử dụng Coil để load an toàn với fallback
        val imagePainter = rememberAsyncImagePainter(
            model = exercise.thumbRes,
            error = painterResource(android.R.drawable.ic_menu_gallery),  // Fallback nếu error
            onError = { Log.e("ExerciseItem", "Error loading thumbRes: ${exercise.thumbRes}, ${it.result.throwable}") }
        )
        Image(
            painter = imagePainter,
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
            // Tương tự cho premium icon
            val iconPainter = rememberAsyncImagePainter(
                model = R.drawable.ic_profile,
                error = painterResource(android.R.drawable.star_big_on),  // Fallback hệ thống
                onError = { Log.e("ExerciseItem", "Error loading ic_profile: ${it.result.throwable}") }
            )
            Icon(painter = iconPainter, contentDescription = "Premium", tint = Color(0xFFFFD700))
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