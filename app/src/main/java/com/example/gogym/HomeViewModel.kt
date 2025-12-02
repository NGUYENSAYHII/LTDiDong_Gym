package com.example.gogym

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogym.data.HomeRepository
import com.example.gogym.model.MealFood
import com.example.gogym.model.MealLog
import com.example.gogym.model.MealType
import com.example.gogym.model.WorkoutLog
import kotlinx.coroutines.launch

data class HomeUiState(
    val totalBurned: Int = 0,
    val totalEaten: Int = 0,
    val goalCalories: Int = 2000,
    val waterLiter: Float = 0f
) {
    val availableCalories: Int
        get() = (totalEaten - totalBurned).coerceAtLeast(0)
}

class HomeViewModel(
    private val repository: HomeRepository = HomeRepository()
) : ViewModel() {

    var uiState = androidx.compose.runtime.mutableStateOf(HomeUiState())
        private set

    fun refreshTodayData() {
        viewModelScope.launch {
            val burned = repository.getTodayBurnedCalories()
            val eaten = repository.getTodayEatenCalories()
            val water = repository.getTodayWaterLiter()
            val goal = repository.getGoalCalories()

            uiState.value = HomeUiState(
                totalBurned = burned,
                totalEaten = eaten,
                goalCalories = goal,
                waterLiter = water
            )
        }
    }

    // ✅ chỉnh Goal kcal (đã làm đúng rồi)
    fun updateGoalCalories(newGoal: Int) {
        uiState.value = uiState.value.copy(goalCalories = newGoal)
        viewModelScope.launch {
            repository.updateGoalCalories(newGoal)
        }
    }

    // ✅ Khi hoàn thành 1 bài tập: update UI NGAY, rồi mới ghi Firestore
    fun logWorkout(exerciseId: String, name: String, calories: Int, durationSec: Int) {
        // 1. Cập nhật UI trước (optimistic)
        val old = uiState.value
        uiState.value = old.copy(
            totalBurned = old.totalBurned + calories
        )

        // 2. Ghi log lên Firestore nền
        viewModelScope.launch {
            repository.addWorkoutLog(
                WorkoutLog(
                    exerciseId = exerciseId,
                    exerciseName = name,
                    caloriesBurned = calories,
                    durationSeconds = durationSec
                )
            )
            // optional: đồng bộ lại từ server (nếu cần tránh lệch)
            // refreshTodayData()
        }
    }

    // ✅ Khi thêm 1 bữa ăn
    fun addMeal(mealType: MealType, foods: List<MealFood>) {
        val totalCalories = foods.sumOf { it.calories * it.amount }
        val totalProtein = foods.sumOf { it.protein * it.amount }

        // 1. Update UI ngay
        val old = uiState.value
        uiState.value = old.copy(
            totalEaten = old.totalEaten + totalCalories
        )

        // 2. Ghi Firestore
        viewModelScope.launch {
            repository.addMealLog(
                MealLog(
                    mealType = mealType,
                    foods = foods,
                    totalCalories = totalCalories,
                    totalProtein = totalProtein
                )
            )
            // refreshTodayData() // nếu muốn sync lại từ server
        }
    }

    // ✅ Khi cộng nước
    fun addWater(amountLiter: Float = 0.2f) {
        // 1. Update UI ngay
        val old = uiState.value
        uiState.value = old.copy(
            waterLiter = old.waterLiter + amountLiter
        )

        // 2. Ghi Firestore
        viewModelScope.launch {
            repository.addWaterLog(amountLiter)
            // refreshTodayData()
        }
    }

    //  Khi trừ nước (lỡ bấm nhầm)
    fun removeWater(amountLiter: Float = 0.2f) {
        val old = uiState.value
        val newAmount = (old.waterLiter - amountLiter).coerceAtLeast(0f)

        // 1. Cập nhật UI
        uiState.value = old.copy(
            waterLiter = newAmount
        )

        // 2. Ghi log Firestore (nếu muốn cho “undo” đúng)
        viewModelScope.launch {
            repository.addWaterLog(-amountLiter)
        }
    }

}
