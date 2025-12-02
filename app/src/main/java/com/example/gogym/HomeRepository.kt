package com.example.gogym.data

import com.example.gogym.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HomeRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    // ✅ biến lưu Goal kcal tạm thời trong RAM
    // (sau này nếu muốn có thể lưu Firestore / SharedPreferences)
    private var goalCalories: Int = 2200

    private fun currentUserId(): String? = auth.currentUser?.uid

    // --- Ghi workout log ---
    suspend fun addWorkoutLog(log: WorkoutLog) {
        val userId = currentUserId() ?: return
        db.collection("users")
            .document(userId)
            .collection("workout_logs")
            .add(
                log.copy(
                    userId = userId,
                    // đảm bảo có timestamp để query "hôm nay"
                    timestamp = log.timestamp ?: System.currentTimeMillis()
                )
            )
            .await()
    }

    // --- Ghi meal log ---
    suspend fun addMealLog(mealLog: MealLog) {
        val userId = currentUserId() ?: return
        db.collection("users")
            .document(userId)
            .collection("meal_logs")
            .add(
                mealLog.copy(
                    userId = userId,
                    timestamp = mealLog.timestamp ?: System.currentTimeMillis()
                )
            )
            .await()
    }

    // --- Ghi water log ---
    suspend fun addWaterLog(amountLiter: Float) {
        val userId = currentUserId() ?: return
        val log = WaterLog(
            userId = userId,
            amountLiter = amountLiter,
            timestamp = System.currentTimeMillis()
        )
        db.collection("users")
            .document(userId)
            .collection("water_logs")
            .add(log)
            .await()
    }

    // --- Lấy tổng calories đã burn hôm nay ---
    suspend fun getTodayBurnedCalories(): Int {
        val userId = currentUserId() ?: return 0
        val startOfDay = getStartOfDayMillis()
        val snapshot = db.collection("users")
            .document(userId)
            .collection("workout_logs")
            .whereGreaterThanOrEqualTo("timestamp", startOfDay)
            .get()
            .await()

        return snapshot.documents.sumOf {
            (it.getLong("caloriesBurned") ?: 0L).toInt()
        }
    }

    // --- Lấy tổng calories đã ăn hôm nay ---
    suspend fun getTodayEatenCalories(): Int {
        val userId = currentUserId() ?: return 0
        val startOfDay = getStartOfDayMillis()
        val snapshot = db.collection("users")
            .document(userId)
            .collection("meal_logs")
            .whereGreaterThanOrEqualTo("timestamp", startOfDay)
            .get()
            .await()

        return snapshot.documents.sumOf {
            (it.getLong("totalCalories") ?: 0L).toInt()
        }
    }

    // --- Lấy tổng nước hôm nay ---
    suspend fun getTodayWaterLiter(): Float {
        val userId = currentUserId() ?: return 0f
        val startOfDay = getStartOfDayMillis()
        val snapshot = db.collection("users")
            .document(userId)
            .collection("water_logs")
            .whereGreaterThanOrEqualTo("timestamp", startOfDay)
            .get()
            .await()

        return snapshot.documents.sumOf {
            (it.getDouble("amountLiter") ?: 0.0)
        }.toFloat()
    }

    // ✅ Lấy / cập nhật Goal kcal
    suspend fun getGoalCalories(): Int = goalCalories

    suspend fun updateGoalCalories(newGoal: Int) {
        goalCalories = newGoal
        // sau này nếu muốn lưu Firestore thì thêm code ở đây
    }

    // --- hỗ trợ: tính mốc đầu ngày ---
    private fun getStartOfDayMillis(): Long {
        val now = java.util.Calendar.getInstance()
        now.set(java.util.Calendar.HOUR_OF_DAY, 0)
        now.set(java.util.Calendar.MINUTE, 0)
        now.set(java.util.Calendar.SECOND, 0)
        now.set(java.util.Calendar.MILLISECOND, 0)
        return now.timeInMillis
    }
}
