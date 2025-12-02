package com.example.gogym.model

data class MealFood(
    val id: String = "",
    val name: String = "",
    val calories: Int = 0,
    val protein: Int = 0,
    val amount: Int = 1,          // số phần / đơn vị
    val unit: String = "portion"  // ví dụ: g, ml, portion
)

data class MealLog(
    val id: String = "",
    val userId: String = "",
    val mealType: MealType = MealType.BREAKFAST,
    val foods: List<MealFood> = emptyList(),
    val totalCalories: Int = 0,
    val totalProtein: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
