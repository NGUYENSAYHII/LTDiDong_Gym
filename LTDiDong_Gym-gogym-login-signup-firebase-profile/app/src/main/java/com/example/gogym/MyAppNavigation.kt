package com.example.gogym.pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gogym.AuthViewModel

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignupPage(modifier, navController, authViewModel)
        }
        composable("home") {
            HomePage(modifier, navController, authViewModel)
        }
        composable("settings") {
            SettingsPage(modifier, navController, authViewModel)
        }

        // Trang Workout – bỏ authViewModel vì WorkoutPage không nhận tham số này
        composable("workout") {
            WorkoutPage(navController = navController, modifier = modifier)
        }

        composable("profile") {
            ProfilePage(modifier, navController, authViewModel)
        }

        // ==== THÊM ROUTE CHI TIẾT CATEGORY ====
        composable("workout_detail/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "abs"
            WorkoutCategoryDetailPage(
                categoryId = categoryId,
                navController = navController
            )
        }

        // ==== THÊM ROUTE CHI TIẾT BÀI TẬP ====
        composable("exercise_detail/{exerciseId}") { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: "abs1"
            ExerciseDetailPage(
                exerciseId = exerciseId,
                navController = navController
            )
        }
    }
}
