package com.example.gogym.pages

import androidx.navigation.NavController

/**
 * Helper để điều hướng tới ExerciseDetailPage với 3 tham số chuẩn:
 *   exerciseId, startImmediately ("true"/"false"), seconds (số giây, 0 = không override)
 *
 * Luôn dùng helper này mọi nơi cần navigate tới ExerciseDetailPage để tránh mismatch route.
 */
fun navigateToExerciseDetail(
    navController: NavController,
    exerciseId: String,
    startImmediately: Boolean = false,
    seconds: Int = 0
) {
    val startParam = if (startImmediately) "true" else "false"
    navController.navigate("exercise_detail/$exerciseId/$startParam/$seconds")
}