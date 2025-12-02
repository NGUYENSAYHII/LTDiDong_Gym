package com.example.gogym.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gogym.AuthViewModel
import com.example.gogym.HomeViewModel
import com.example.gogym.model.MealType   // 👈 cần import cái này
import com.example.gogym.pages.HomePage
@Composable
fun MainTabsPage(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel
) {
    val bottomNavController: NavHostController = rememberNavController()

    val items = remember {
        listOf(
            BottomBarItem(
                route = "tab_home",
                label = "Kế hoạch",
                icon = Icons.Default.Home
            ),
            BottomBarItem(
                route = "tab_customize",
                label = "Tùy chỉnh",
                icon = Icons.Default.List
            ),
            BottomBarItem(
                route = "tab_workout",
                label = "Bài tập",
                icon = Icons.Default.Star
            ),
            BottomBarItem(
                route = "tab_profile",
                label = "Cá Nhân",
                icon = Icons.Default.Settings
            ),
        )
    }

    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            FancyBottomBar(
                items = items,
                currentDestination = currentDestination,
                onItemClick = { item ->
                    bottomNavController.navigate(item.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "tab_home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("tab_home") {
                HomePage(
                    navController = bottomNavController,
                    homeViewModel = homeViewModel
                )
            }

            composable("tab_customize") {
                CustomizePage(
                    onCustomPlanClick = {
                        bottomNavController.navigate("custom_plan_detail")
                    },
                    onOfficeWorkoutClick = {
                        bottomNavController.navigate("office_workout")
                    },
                    onAutoWorkoutClick = {
                        bottomNavController.navigate("auto_workout")
                    },
                    onReportClick = {
                        bottomNavController.navigate("workout_report")
                    }
                )
            }

            composable("tab_workout") {
                WorkoutPage(
                    navController = bottomNavController,
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable("tab_profile") {
                ProfilePage(
                    modifier = Modifier.fillMaxSize(),
                    navController = rootNavController,
                    authViewModel = authViewModel
                )
            }

            composable("custom_plan_detail") {
                CustomPlanDetailPage(
                    navController = bottomNavController
                )
            }

            composable("office_workout") {
                OfficeWorkoutPage(
                    navController = bottomNavController
                )
            }

            composable("workout_report") {
                WorkoutReportPage(
                    navController = bottomNavController
                )
            }

            composable("auto_workout") {
                AutoWorkoutPage(
                    navController = bottomNavController
                )
            }

            //  màn thêm bữa ăn
            composable(
                route = "add_meal/{mealType}",
                arguments = listOf(
                    navArgument("mealType") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val typeName = backStackEntry.arguments?.getString("mealType") ?: "BREAKFAST"
                val mealType = MealType.valueOf(typeName)

                AddFoodPage(
                    navController = bottomNavController,
                    mealType = mealType,
                    homeViewModel = homeViewModel
                )
            }   //  nhớ đóng ngoặc của composable này

            // Chi tiết danh mục bài tập
            composable(
                route = "workout_detail/{categoryId}",
                arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
            ) { backStackEntry ->
                val categoryId =
                    backStackEntry.arguments?.getString("categoryId") ?: "abs"
                WorkoutCategoryDetailPage(
                    categoryId = categoryId,
                    navController = bottomNavController
                )
            }

            // Chi tiết bài tập
            composable(
                route = "exercise_detail/{exerciseId}/{startImmediately}/{seconds}",
                arguments = listOf(
                    navArgument("exerciseId") { type = NavType.StringType },
                    navArgument("startImmediately") {
                        type = NavType.StringType
                        defaultValue = "false"
                    },
                    navArgument("seconds") {
                        type = NavType.StringType
                        defaultValue = "0"
                    }
                )
            ) { backStackEntry ->
                val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: "abs1"
                val startParam = backStackEntry.arguments?.getString("startImmediately") ?: "false"
                val startImmediately = startParam.equals("true", ignoreCase = true)
                val secondsParam = backStackEntry.arguments?.getString("seconds") ?: "0"
                val initialSeconds = secondsParam.toIntOrNull()?.takeIf { it > 0 }

                ExerciseDetailPage(
                    exerciseId = exerciseId,
                    navController = bottomNavController,
                    startImmediately = startImmediately,
                    initialSeconds = initialSeconds,
                    homeViewModel = homeViewModel
                )
            }
        }
    }
}

@Composable
fun HomeTabContent(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel      // hiện tại chưa dùng tới cũng được
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home Page",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = { authViewModel.signout() }) {
            Text("Sign Out")
        }
    }
}

data class BottomBarItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun FancyBottomBar(
    items: List<BottomBarItem>,
    currentDestination: NavDestination?,
    onItemClick: (BottomBarItem) -> Unit
) {
    val selectedColor = Color(0xFFFF4B4B)
    val unselectedColor = Color(0xFF888888)
    val backgroundColor = Color.White.copy(alpha = 0.98f)
    val borderColor = Color(0xFFE0E0E0)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->
                val selected = currentDestination
                    ?.hierarchy
                    ?.any { it.route == item.route } == true

                NavigationBarItem(
                    selected = selected,
                    onClick = { onItemClick(item) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (selected) selectedColor else unselectedColor
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            color = if (selected) selectedColor else unselectedColor
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0x1AFF4B4B),
                        selectedIconColor = selectedColor,
                        selectedTextColor = selectedColor,
                        unselectedIconColor = unselectedColor,
                        unselectedTextColor = unselectedColor
                    )
                )
            }
        }
    }
}
