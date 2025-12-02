package com.example.gogym.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.gogym.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    // Nếu user đã đăng nhập trước đó -> vào luôn mainTabs
    val startDestination = remember {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) "mainTabs" else "login"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable("signup") {
            SignupPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // Shell chứa 4 tab + bottom bar
        composable("mainTabs") {
            MainTabsPage(
                rootNavController = navController,   // nav gốc của app
                authViewModel = authViewModel
            )
        }

        // Các màn chi tiết (nếu có) – dùng nav gốc luôn
        composable("workout_detail/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "abs"
            WorkoutCategoryDetailPage(
                categoryId = categoryId,
                navController = navController,
                rootNavController = navController // truyền rõ ràng root controller
            )
        }

        // Đồng bộ: khai báo exercise_detail với 3 tham số ở NavHost gốc để helper gọi trên rootNavController cũng hợp lệ.
        composable(
            route = "exercise_detail/{exerciseId}/{startImmediately}/{seconds}",
            arguments = listOf(
                navArgument("exerciseId") { type = NavType.StringType },
                navArgument("startImmediately") {
                    type = NavType.StringType
                    defaultValue = "false"
                },
                // seconds nên là IntType (để đọc trực tiếp bằng getInt)
                navArgument("seconds") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: "abs1"
            val startParam = backStackEntry.arguments?.getString("startImmediately") ?: "false"
            val startImmediately = startParam.equals("true", ignoreCase = true)
            val secondsParam = backStackEntry.arguments?.getInt("seconds") ?: 0
            val initialSeconds = secondsParam.takeIf { it > 0 }

            ExerciseDetailPage(
                exerciseId = exerciseId,
                navController = navController,
                startImmediately = startImmediately,
                initialSeconds = initialSeconds
            )
        }
    }
}