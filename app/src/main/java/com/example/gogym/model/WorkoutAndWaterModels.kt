package com.example.gogym.model

data class WorkoutLog(
    val id: String = "",
    val userId: String = "",
    val exerciseId: String = "",
    val exerciseName: String = "",
    val caloriesBurned: Int = 0,
    val durationSeconds: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

data class WaterLog(
    val id: String = "",
    val userId: String = "",
    val amountLiter: Float = 0.2f,
    val timestamp: Long = System.currentTimeMillis()
)
