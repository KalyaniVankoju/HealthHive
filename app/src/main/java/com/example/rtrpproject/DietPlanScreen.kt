package com.example.rtrpproject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun DietPlanScreen(navController: NavHostController) {

    val goalOptions = listOf("Weight Loss", "Weight Gain", "Muscle Gain", "Maintenance")
    val conditionOptions = listOf("None", "Diabetes", "BP", "PCOS", "Heart")
    val foodOptions = listOf("Vegetarian", "Vegan", "Non-Vegetarian")
    val nutrientOptions = listOf("High Protein", "Low Carb", "Balanced Diet")
    val mealPatternOptions = listOf("Regular Meals", "Intermittent Fasting", "Small Frequent Meals")
    val activityOptions = listOf("Sedentary", "Moderately Active", "Highly Active")

    var selectedGoal by remember { mutableStateOf("") }
    var selectedCondition by remember { mutableStateOf("") }
    var selectedFood by remember { mutableStateOf("") }
    var selectedNutrient by remember { mutableStateOf("") }
    var selectedMealPattern by remember { mutableStateOf("") }
    var selectedActivity by remember { mutableStateOf("") }

    var result by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF3F6FF),
                        Color(0xFFEDE7F6)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Advanced Diet Plan 🥗",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5E35B1)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Fill all details to get a personalized diet plan",
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.92f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    DropdownField("Goal", goalOptions, selectedGoal) {
                        selectedGoal = it
                        message = ""
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DropdownField("Health Condition", conditionOptions, selectedCondition) {
                        selectedCondition = it
                        message = ""
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DropdownField("Food Preference", foodOptions, selectedFood) {
                        selectedFood = it
                        message = ""
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DropdownField("Nutrient Type", nutrientOptions, selectedNutrient) {
                        selectedNutrient = it
                        message = ""
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DropdownField("Meal Pattern", mealPatternOptions, selectedMealPattern) {
                        selectedMealPattern = it
                        message = ""
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DropdownField("Activity Level", activityOptions, selectedActivity) {
                        selectedActivity = it
                        message = ""
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (
                                selectedGoal.isBlank() ||
                                selectedCondition.isBlank() ||
                                selectedFood.isBlank() ||
                                selectedNutrient.isBlank() ||
                                selectedMealPattern.isBlank() ||
                                selectedActivity.isBlank()
                            ) {
                                message = "Please select all fields"
                                result = ""
                            } else {
                                result = generateDietPlan(
                                    selectedGoal,
                                    selectedFood,
                                    selectedNutrient,
                                    selectedActivity,
                                    selectedCondition,
                                    selectedMealPattern
                                )
                                message = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7E57C2)
                        )
                    ) {
                        Text(
                            text = "Generate Diet Plan",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }

                    if (message.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            if (result.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Text(
                            text = "Your Diet Plan",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5E35B1)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFEFFAF3)
                            )
                        ) {
                            Text(
                                text = result,
                                modifier = Modifier.padding(16.dp),
                                fontSize = 15.sp,
                                color = Color(0xFF4B5563),
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("healthhome") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7E57C2)
                )
            ) {
                Text("Back to Dashboard", color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selectedValue: String,
    onValueSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            placeholder = { Text("Select $label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF7E57C2),
                focusedLabelColor = Color(0xFF7E57C2),
                cursorColor = Color(0xFF7E57C2)
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onValueSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun generateDietPlan(
    goal: String,
    foodPreference: String,
    nutrientType: String,
    activityLevel: String,
    condition: String,
    mealPattern: String
): String {

    var plan = "PERSONALIZED DIET PLAN\n\n"

    plan += "Goal: $goal\n"
    plan += "Health Condition: $condition\n"
    plan += "Food Type: $foodPreference\n"
    plan += "Nutrition: $nutrientType\n"
    plan += "Meal Pattern: $mealPattern\n"
    plan += "Activity Level: $activityLevel\n\n"

    plan += when (goal) {
        "Weight Loss" -> "Eat salads, fruits, lean protein. Avoid sugar and fried food.\n\n"
        "Weight Gain" -> "Eat calorie-rich foods like milk, nuts, rice.\n\n"
        "Muscle Gain" -> "Focus on high protein foods like eggs, chicken, paneer.\n\n"
        else -> "Maintain balanced meals with proper nutrients.\n\n"
    }

    plan += "Drink 2-3L water daily\nExercise regularly"

    return plan
}