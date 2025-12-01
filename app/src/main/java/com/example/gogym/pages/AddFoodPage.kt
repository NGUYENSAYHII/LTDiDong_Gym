package com.example.gogym.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gogym.HomeViewModel
import com.example.gogym.model.MealFood
import com.example.gogym.model.MealType

private val LightGreen = Color(0xFFEAFBF2)
private val PrimaryGreen = Color(0xFF4CD964)
private val CardGrey = Color(0xFFF4F4F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodPage(
    navController: NavHostController,
    mealType: MealType,
    homeViewModel: HomeViewModel
) {
    // demo data
    val allFoods = listOf(
        MealFood(id = "1", name = "Coffee with milk", calories = 219, protein = 4),
        MealFood(id = "2", name = "Sandwich", calories = 300, protein = 10),
        MealFood(id = "3", name = "Tomato", calories = 50, protein = 1),
        MealFood(id = "4", name = "Cucumber", calories = 50, protein = 1),
        MealFood(id = "5", name = "Tea (no sugar)", calories = 0, protein = 0),
        MealFood(id = "6", name = "Boiled egg", calories = 98, protein = 8)
    )

    var search by remember { mutableStateOf("") }
    var selectedFoods by remember { mutableStateOf<Map<String, MealFood>>(emptyMap()) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Foods, 1: Favorites, 2: Dishes

    val filteredFoods = remember(search) {
        if (search.isBlank()) allFoods
        else allFoods.filter { it.name.contains(search, ignoreCase = true) }
    }

    val totalSelectedCalories = selectedFoods.values.sumOf { it.calories * it.amount }

    val titleText = when (mealType) {
        MealType.BREAKFAST -> "Breakfast"
        MealType.LUNCH -> "Lunch"
        MealType.DINNER -> "Dinner"
        MealType.SNACK -> "Snack"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = titleText,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            // thanh dưới giống “Add to breakfast” màu xanh
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F7FA))
            ) {
                Button(
                    onClick = {
                        val finalList = selectedFoods.values.toList()
                        if (finalList.isNotEmpty()) {
                            homeViewModel.addMeal(mealType, finalList)
                        }
                        navController.popBackStack()
                    },
                    enabled = totalSelectedCalories > 0,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        disabledContainerColor = Color(0xFFC8EFD4)
                    )
                ) {
                    val label = when (mealType) {
                        MealType.BREAKFAST -> "Add to breakfast"
                        MealType.LUNCH -> "Add to lunch"
                        MealType.DINNER -> "Add to dinner"
                        MealType.SNACK -> "Add to snack"
                    }
                    Text(
                        text = "$label • $totalSelectedCalories kcal",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(LightGreen, Color.White)
                    )
                )
        ) {

            // SEARCH
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp)),
                placeholder = { Text("What did you eat?") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            Spacer(Modifier.height(4.dp))

            // TABS: Foods / Favorites / Dishes
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                contentColor = PrimaryGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab]),
                        color = PrimaryGreen
                    )
                }
            ) {
                listOf("Foods", "Favorites", "Dishes").forEachIndexed { index, label ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = label,
                                color = if (selectedTab == index)
                                    PrimaryGreen else Color.Gray
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // LIST
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                items(filteredFoods) { food ->
                    FoodRow(
                        food = food,
                        current = selectedFoods[food.id],
                        onChange = { updated ->
                            selectedFoods = if (updated == null || updated.amount <= 0) {
                                selectedFoods - food.id
                            } else {
                                selectedFoods + (food.id to updated)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun FoodRow(
    food: MealFood,
    current: MealFood?,
    onChange: (MealFood?) -> Unit
) {
    val amount = current?.amount ?: 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardGrey
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${food.calories} kcal",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // nút -
                IconButton(
                    onClick = {
                        if (amount > 0) onChange(food.copy(amount = amount - 1))
                    },
                    enabled = amount > 0,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Text(
                        text = "-",
                        textAlign = TextAlign.Center
                    )
                }

                Text(
                    text = amount.toString(),
                    modifier = Modifier
                        .width(28.dp)
                        .padding(horizontal = 4.dp),
                    textAlign = TextAlign.Center
                )

                // nút +
                IconButton(
                    onClick = {
                        onChange(food.copy(amount = amount + 1))
                    },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Text(
                        text = "+",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
