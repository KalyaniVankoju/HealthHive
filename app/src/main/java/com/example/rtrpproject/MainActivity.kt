package com.example.rtrpproject

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.util.Calendar

class MainActivity : ComponentActivity() {

    private val medicineViewModel: MedicineViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val waterViewModel: WaterViewModel by viewModels()
    private val stepViewModel: StepViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    100
                )
            }
        }

        setContent {
            MaterialTheme {
                AppNavigation(
                    medicineViewModel = medicineViewModel,
                    userViewModel = userViewModel,
                    waterViewModel = waterViewModel,
                    stepViewModel = stepViewModel
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    medicineViewModel: MedicineViewModel,
    userViewModel: UserViewModel,
    waterViewModel: WaterViewModel,
    stepViewModel: StepViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        composable("login") {
            LoginScreen(navController, userViewModel)
        }

        composable("signup") {
            SignupScreen(navController, userViewModel)
        }

        composable("home") {
            HomePage(navController)
        }

        composable("healthhome") {
            HealthHomeScreen(
                navController = navController,
                waterViewModel = waterViewModel,
                stepViewModel = stepViewModel
            )
        }

        composable("bmi") {
            BMIScreen(navController)
        }

        composable("steps") {
            StepScreen(
                navController = navController,
                viewModel = stepViewModel
            )
        }
        composable("stepsWeekly") {
            StepsWeeklyScreen(
                navController = navController,
                viewModel = stepViewModel
            )
        }

        composable("water") {
            WaterScreen(
                navController = navController,
                viewModel = waterViewModel
            )
        }
        composable("waterWeekly") {
            WaterWeeklyScreen(
                navController = navController,
                viewModel = waterViewModel
            )
        }
        composable("analysis") {
            SkinHairAnalysisScreen(navController)
        }

        composable("medicine") {
            MedicineReminderScreen(
                navController = navController,
                viewModel = medicineViewModel
            )
        }

        composable("dietplan") {
            DietPlanScreen(navController)
        }

        composable("chatbot") {
            ChatbotScreen(navController)
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController, viewModel: UserViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF6F8F2),
                        Color(0xFFEDE7F6)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Welcome Back",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1F1F)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Login to continue your wellness journey",
                fontSize = 15.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            if (message.isNotEmpty()) message = ""
                        },
                        label = { Text("Username") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Username"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7E57C2),
                            focusedLabelColor = Color(0xFF7E57C2),
                            cursorColor = Color(0xFF7E57C2)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (message.isNotEmpty()) message = ""
                        },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) {
                                        Icons.Default.Visibility
                                    } else {
                                        Icons.Default.VisibilityOff
                                    },
                                    contentDescription = "Toggle Password Visibility"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7E57C2),
                            focusedLabelColor = Color(0xFF7E57C2),
                            cursorColor = Color(0xFF7E57C2)
                        )
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = {
                            val enteredUsername = username.trim()
                            val enteredPassword = password.trim()

                            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                                message = "Enter all fields"
                            } else {
                                viewModel.loginUser(enteredUsername, enteredPassword) { success ->
                                    if (success) {
                                        message = ""
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        message = "Invalid credentials"
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7E57C2)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Text(
                            text = "Login",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    if (message.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color(0xFF6B7280),
                    fontSize = 16.sp
                )

                TextButton(
                    onClick = { navController.navigate("signup") },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Sign Up",
                        color = Color(0xFF7E57C2),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SignupScreen(navController: NavHostController, viewModel: UserViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF6F8F2),
                        Color(0xFFEDE7F6)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Create Account",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1F1F)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sign up to start your wellness journey",
                fontSize = 15.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            if (message.isNotEmpty()) message = ""
                        },
                        label = { Text("Username") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Username"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7E57C2),
                            focusedLabelColor = Color(0xFF7E57C2),
                            cursorColor = Color(0xFF7E57C2)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (message.isNotEmpty()) message = ""
                        },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) {
                                        Icons.Default.Visibility
                                    } else {
                                        Icons.Default.VisibilityOff
                                    },
                                    contentDescription = "Toggle Password Visibility"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7E57C2),
                            focusedLabelColor = Color(0xFF7E57C2),
                            cursorColor = Color(0xFF7E57C2)
                        )
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = {
                            val enteredUsername = username.trim()
                            val enteredPassword = password.trim()

                            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                                message = "Enter all fields"
                            } else {
                                viewModel.checkUserExists(enteredUsername) { exists ->
                                    if (exists) {
                                        message = "User already exists"
                                    } else {
                                        viewModel.insertUser(User(enteredUsername, enteredPassword)) {
                                            message = "Account created successfully"
                                            navController.navigate("home") {
                                                popUpTo("signup") { inclusive = true }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7E57C2)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Text(
                            text = "Create Account",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    if (message.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = message,
                            color = if (message.contains("successfully")) {
                                Color(0xFF7E57C2)
                            } else {
                                MaterialTheme.colorScheme.error
                            },
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    color = Color(0xFF6B7280),
                    fontSize = 16.sp
                )

                TextButton(
                    onClick = { navController.navigate("login") },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Login",
                        color = Color(0xFF7E57C2),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun HomePage(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
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
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HealthHive",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF5E35B1)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "All your health, in one place",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome 👋",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Track your health & stay fit",
                        fontSize = 15.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = { navController.navigate("healthhome"){
                            popUpTo(0)


                        } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7E57C2)
                        )
                    ) {
                        Text(
                            text = "Go to Dashboard",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = { navController.navigate("login") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(30.dp),
                        border = BorderStroke(1.5.dp, Color(0xFF7E57C2)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF7E57C2)
                        )
                    ) {
                        Text(
                            text = "Logout",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HealthHomeScreen(
    navController: NavHostController,
    waterViewModel: WaterViewModel,
    stepViewModel: StepViewModel
) {
    val greeting = getGreeting()
    val today = getTodayDate()
    val waterList by waterViewModel.getTodayWaterEntries(today).observeAsState(emptyList())
    val totalWater = waterList.sumOf { it.amount }

    val stepList by stepViewModel.getTodayStepEntries(today).observeAsState(emptyList())
    val totalSteps = stepList.sumOf { it.steps }
    val caloriesBurned = totalSteps * 0.04
    val waterProgress = (totalWater / 3000f).coerceIn(0f, 1f)
//LaunchedEffect(Unit){
  //  stepViewModel.insertDummyStepsOnce()
    //waterViewModel.insertDummyWaterOnce()
//}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF7F8F3),
                        Color(0xFFF1EDF9)
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Health Tracker",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2E7D32)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$greeting 👋",
                    fontSize = 18.sp,
                    color = Color(0xFF4B5563),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Track your wellness and stay consistent every day.",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            HealthCard(
                title = "Steps",
                value = "$totalSteps",
                subtitle = "today",
                emoji = "🚶",
                color = Color(0xFFEAF4FF),
                modifier = Modifier.weight(1f),
                onClick = {
                    navController.navigate("stepsWeekly")
                }
            )

            HealthCard(
                title = "Calories",
                value = "%.0f".format(caloriesBurned),
                subtitle = "kcal burned",
                emoji = "🔥",
                color = Color(0xFFFFF1E6),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            onClick = {
                navController.navigate("waterWeekly")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF7FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    text = "💧 Water Intake",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$totalWater ml today",
                    fontSize = 18.sp,
                    color = Color(0xFF374151),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = { waterProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(50.dp)),
                    color = Color(0xFF7E57C2),
                    trackColor = Color(0xFFD9CFF1)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Daily goal: 3000 ml",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Quick Actions",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = Color(0xFF111827)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "BMI",
                emoji = "⚖️",
                modifier = Modifier.weight(1f)
            ) {
                navController.navigate("bmi")
            }

            QuickActionCard(
                title = "Water",
                emoji = "💧",
                modifier = Modifier.weight(1f)
            ) {
                navController.navigate("water")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "Steps",
                emoji = "🚶",
                modifier = Modifier.weight(1f)
            ) {
                navController.navigate("steps")
            }

            QuickActionCard(
                title = "Medicine",
                emoji = "💊",
                modifier = Modifier.weight(1f)
            ) {
                navController.navigate("medicine")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "Analysis",
                emoji = "✨",
                modifier = Modifier.weight(1f)
            ) {
                navController.navigate("analysis")
            }

            QuickActionCard(
                title = "Diet Plan",
                emoji = "🥗",
                modifier = Modifier.weight(1f)
            ) {
                navController.navigate("dietplan")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionWideCard(
            title = "Health Chatbot",
            emoji = "🤖"
        ) {
            navController.navigate("chatbot")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun BMIScreen(navController: NavHostController) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf(0f) }
    var category by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val animatedProgress by animateFloatAsState(
        targetValue = (bmi / 40f).coerceIn(0f, 1f),
        animationSpec = tween(800),
        label = "bmiAnim"
    )

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
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "BMI Calculator",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5E35B1)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = {
                            weight = it
                            errorMessage = ""
                        },
                        label = { Text("Weight (kg)") },
                        leadingIcon = { Text("⚖️", fontSize = 20.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = height,
                        onValueChange = {
                            height = it
                            errorMessage = ""
                        },
                        label = { Text("Height (m)") },
                        leadingIcon = { Text("📏", fontSize = 20.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            val w = weight.toFloatOrNull()
                            val h = height.toFloatOrNull()

                            if (w == null || h == null || w <= 0f || h <= 0f) {
                                bmi = 0f
                                category = ""
                                errorMessage = "Please enter valid weight and height"
                            } else {
                                bmi = w / (h * h)
                                category = when {
                                    bmi < 18.5f -> "Underweight"
                                    bmi < 24.9f -> "Healthy"
                                    bmi < 29.9f -> "Overweight"
                                    else -> "Obese"
                                }
                                errorMessage = ""
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
                            text = "Calculate BMI",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            if (bmi > 0f) {
                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your BMI",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "%.2f".format(bmi),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF5E35B1)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = category,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = when (category) {
                                "Underweight" -> Color(0xFF1E88E5)
                                "Healthy" -> Color(0xFF2E7D32)
                                "Overweight" -> Color(0xFFF9A825)
                                else -> Color(0xFFC62828)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(50.dp)),
                            color = Color(0xFF7E57C2),
                            trackColor = Color(0xFFD9CFF1)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("0", fontSize = 12.sp)
                            Text("18.5", fontSize = 12.sp)
                            Text("25", fontSize = 12.sp)
                            Text("30", fontSize = 12.sp)
                            Text("40", fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = when (category) {
                                "Underweight" -> "You may need to gain some healthy weight."
                                "Healthy" -> "Great! Your BMI is in the healthy range."
                                "Overweight" -> "Try regular exercise and a balanced diet."
                                else -> "Consider a healthier lifestyle and consult a doctor if needed."
                            },
                            fontSize = 14.sp,
                            color = Color(0xFF4B5563),
                            textAlign = TextAlign.Center
                        )
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
                Text(
                    text = "Back to Dashboard",
                    fontSize = 17.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StepScreen(
    navController: NavHostController,
    viewModel: StepViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val intent = Intent(context, StepForegroundService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }
    var stepsInput by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val today = getTodayDate()
    val stepList by viewModel.getTodayStepEntries(today).observeAsState(emptyList())
    val totalSteps = stepList.sumOf { it.steps }
    val caloriesBurned = totalSteps * 0.04
    val stepProgress = (totalSteps / 10000f).coerceIn(0f, 1f)

    val goalLeft = (10000 - totalSteps).coerceAtLeast(0)
    val goalPercent = ((totalSteps / 10000f) * 100).coerceIn(0f, 100f)

    val motivationText = when {
        totalSteps == 0 -> "Let's start walking today!"
        totalSteps < 3000 -> "Good start, keep moving!"
        totalSteps < 7000 -> "Nice progress, you're doing well!"
        totalSteps < 10000 -> "Almost there, keep it up!"
        else -> "Awesome! Daily goal achieved!"
    }


    message = "Background step tracking active"

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
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Steps Tracker 🚶",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5E35B1)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 AUTO TRACKING CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF7FF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    Text(
                        text = "Auto Tracking (Live)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$totalSteps steps",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF5E35B1)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "%.1f%% of daily goal".format(goalPercent),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 MANUAL ENTRY CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.92f)
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    OutlinedTextField(
                        value = stepsInput,
                        onValueChange = {
                            stepsInput = it
                            message = ""
                        },
                        label = { Text("Add missed steps manually") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            val steps = stepsInput.toIntOrNull()
                            if (steps != null && steps > 0) {
                                viewModel.insertSteps(steps)
                                stepsInput = ""
                                message = "Steps added successfully"
                            } else {
                                message = "Enter valid steps"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text("Add Steps")
                    }

                    if (message.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = message,
                            color = if (message.contains("success")) Color.Green else Color.Red
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 SUMMARY CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Today's Summary",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "$totalSteps steps",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5E35B1)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Calories: %.2f kcal".format(caloriesBurned))
                    Text("Goal left: $goalLeft steps")

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { stepProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(50.dp))
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = motivationText,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF5E35B1)
                    )
                }
            }

            // 🔥 GOAL ACHIEVED CARD
            if (totalSteps >= 10000) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "🎉 Goal Achieved! Great job!",
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("healthhome") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Dashboard")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
@Composable
fun StepsWeeklyScreen(
    navController: NavHostController,
    viewModel: StepViewModel
) {
    val today = getTodayDate()
    val todayStepsList by viewModel.getTodayStepEntries(today).observeAsState(emptyList())
    val totalStepsToday = todayStepsList.sumOf { it.steps }

    val weeklySteps by viewModel.getLast7DaysSteps().observeAsState(emptyList())
    val chartData = getLast7DaysWithZeros(weeklySteps)

    val weeklyTotal = chartData.sumOf { it.steps }
    val activeDays = chartData.count { it.steps > 0 }
    val avgActiveSteps = if (activeDays > 0) weeklyTotal / activeDays else 0
    val avgWeeklySteps = weeklyTotal / 7
    val bestDay = chartData.maxByOrNull { it.steps }
    val goalAchievedDays = chartData.count { it.steps >= 10000 }

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
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Weekly Step Analytics",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF5E35B1)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Track your weekly walking pattern",
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "🚶 Today's Steps",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "$totalStepsToday",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF5E35B1)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Daily Avg", fontSize = 14.sp, color = Color(0xFF6B7280))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$avgWeeklySteps",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5E35B1)
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Active Avg", fontSize = 14.sp, color = Color(0xFF6B7280))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$avgActiveSteps",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5E35B1)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "⭐ Weekly Highlights",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Best Day: ${formatDisplayDate(bestDay?.date ?: "")}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5E35B1)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Best Step Count: ${bestDay?.steps ?: 0}",
                        fontSize = 16.sp,
                        color = Color(0xFF4B5563)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Goal Achieved Days: $goalAchievedDays",
                        fontSize = 16.sp,
                        color = Color(0xFF4B5563)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "📊 Last 7 Days",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    WeeklyBarChart(weeklySteps = chartData)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Purple bars show daily step count",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
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
                Text("Back to Dashboard", fontSize = 17.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun WaterWeeklyScreen(
    navController: NavHostController,
    viewModel: WaterViewModel
) {
    val today = getTodayDate()
    val todayWaterList by viewModel.getTodayWaterEntries(today).observeAsState(emptyList())
    val totalToday = todayWaterList.sumOf { it.amount }

    val weeklyEntries by viewModel.getLast7DaysWater().observeAsState(emptyList())

    val grouped = weeklyEntries.groupBy { it.date }
        .map { (date, list) ->
            StepEntry(date = date, steps = list.sumOf { it.amount })
        }

    val chartData = getLast7DaysWithZeros(grouped)

    val weeklyTotal = chartData.sumOf { it.steps }
    val activeDays = chartData.count { it.steps > 0 }
    val avgActive = if (activeDays > 0) weeklyTotal / activeDays else 0
    val avgWeekly = weeklyTotal / 7
    val bestDay = chartData.maxByOrNull { it.steps }
    val goalDays = chartData.count { it.steps >= 3000 }
    val todayGoalPercent = ((totalToday / 3000f) * 100).toInt().coerceIn(0, 100)

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
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Weekly Water Analytics",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1976D2)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Track your weekly water intake pattern",
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "💧 Today's Intake",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "$totalToday ml",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1976D2)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "$todayGoalPercent% of daily goal",
                        fontSize = 13.sp,
                        color = Color(0xFF1976D2)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Daily Avg",
                    value = "$avgWeekly ml",
                    modifier = Modifier.weight(1f),
                    valueColor = Color(0xFF1976D2)
                )

                StatCard(
                    title = "Active Avg",
                    value = "$avgActive ml",
                    modifier = Modifier.weight(1f),
                    valueColor = Color(0xFF1976D2)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "⭐ Weekly Highlights",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Best Day: ${formatDisplayDate(bestDay?.date ?: "")}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Max Intake: ${bestDay?.steps ?: 0} ml",
                        fontSize = 16.sp,
                        color = Color(0xFF4B5563)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Goal Achieved Days: $goalDays",
                        fontSize = 16.sp,
                        color = Color(0xFF4B5563)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "📊 Last 7 Days",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    WaterBarChart(data = chartData)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Blue bars show daily water intake",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
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
                    containerColor = Color(0xFF1976D2)
                )
            ) {
                Text("Back to Dashboard", fontSize = 17.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
@Composable
fun WaterBarChart(data: List<StepEntry>) {
    val max = (data.maxOfOrNull { it.steps } ?: 1).coerceAtLeast(1)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { entry ->
            val fraction = (entry.steps / max.toFloat()).coerceIn(0f, 1f)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.height(240.dp)
            ) {
                Text(
                    text = if (entry.steps == 0) "" else entry.steps.toString(),
                    fontSize = 11.sp,
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .height(160.dp)
                        .width(22.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (entry.steps == 0) 12.dp else 160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE3F2FD))
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                if (entry.steps == 0) 2.dp
                                else (150 * fraction).dp
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF2196F3))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatShortDay(entry.date),
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563)
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color(0xFF1976D2)
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}
@Composable
fun WeeklyBarChart(
    weeklySteps: List<StepEntry>
) {
    val maxSteps = (weeklySteps.maxOfOrNull { it.steps } ?: 1).coerceAtLeast(1)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        weeklySteps.forEach { entry ->
            val barFraction = (entry.steps.toFloat() / maxSteps).coerceIn(0f, 1f)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.height(240.dp)
            ) {
                Text(
                    text = if (entry.steps == 0) "" else entry.steps.toString(),
                    fontSize = 11.sp,
                    color = Color(0xFF5E35B1),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .height(160.dp)
                        .width(28.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (entry.steps == 0) 12.dp else 160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE9E1F7))
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                if (entry.steps == 0) 2.dp
                                else (150 * barFraction).dp
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF7E57C2))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatShortDay(entry.date),
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563)
                )
            }
        }
    }
}
@Composable
fun HealthCard(
    title: String,
    value: String,
    subtitle: String,
    emoji: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        onClick = { onClick?.invoke() },
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = emoji,
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    emoji: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF7E57C2),
                            Color(0xFF5E35B1)
                        )
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = emoji,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun QuickActionWideCard(
    title: String,
    emoji: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6A40C9),
                            Color(0xFF512DA8)
                        )
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun WaterScreen(
    navController: NavHostController,
    viewModel: WaterViewModel
) {
    var waterInput by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val today = getTodayDate()
    val waterList by viewModel.getTodayWaterEntries(today).observeAsState(emptyList())
    val totalWater = waterList.sumOf { it.amount }
    val waterProgress = (totalWater / 3000f).coerceIn(0f, 1f)

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
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Water Intake Tracker 💧",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5E35B1)
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
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    OutlinedTextField(
                        value = waterInput,
                        onValueChange = {
                            waterInput = it
                            if (message.isNotEmpty()) message = ""
                        },
                        label = { Text("Enter Water (ml)") },
                        leadingIcon = {
                            Text(
                                text = "💧",
                                fontSize = 20.sp
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            val amount = waterInput.toIntOrNull()
                            if (amount != null && amount > 0) {
                                viewModel.insertWater(amount)
                                waterInput = ""
                                message = "Water added successfully"
                            } else {
                                message = "Enter valid water amount"
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
                            text = "Add Water",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }

                    if (message.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = message,
                            color = if (message.contains("successfully")) {
                                Color(0xFF2E7D32)
                            } else {
                                MaterialTheme.colorScheme.error
                            },
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Today's Water Progress",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "$totalWater ml",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF5E35B1)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { waterProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(50.dp)),
                        color = Color(0xFF7E57C2),
                        trackColor = Color(0xFFD9CFF1)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Daily goal: 3000 ml",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
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
                Text(
                    text = "Back to Dashboard",
                    fontSize = 17.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SkinHairAnalysisScreen(navController: NavHostController) {
    val hairOptions = listOf("Oily / Greasy", "Dry / Frizzy", "Prone to Dandruff", "Thin / Falling")
    val skinOptions = listOf("Oily / Acne-Prone", "Dry / Flaky", "Combination", "Sensitive")

    val hairData = mapOf(
        "Oily / Greasy" to Pair("Wash with clarifying shampoo.", "Apple Cider Vinegar rinse."),
        "Dry / Frizzy" to Pair("Use deep conditioning masks.", "Warm Coconut oil massage."),
        "Prone to Dandruff" to Pair("Anti-fungal scalp treatment.", "Aloe Vera + Tea Tree oil."),
        "Thin / Falling" to Pair("Scalp stimulation and biotin.", "Onion juice treatment.")
    )

    val skinData = mapOf(
        "Oily / Acne-Prone" to Pair("Use salicylic acid cleansers.", "Multani Mitti face pack."),
        "Dry / Flaky" to Pair("Use hyaluronic moisturizers.", "Honey & Avocado mask."),
        "Combination" to Pair("Balance T-zone toner.", "Yogurt & Oats scrub."),
        "Sensitive" to Pair("Fragrance-free products.", "Cucumber or Green Tea mist.")
    )

    var selectedHair by remember { mutableStateOf("") }
    var selectedSkin by remember { mutableStateOf("") }
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
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Hair & Skin Analysis ✨",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5E35B1)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Choose your hair and skin type to get a simple care plan",
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
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    SimpleDropdown(
                        label = "Hair Type",
                        options = hairOptions,
                        selectedOption = selectedHair,
                        onOptionSelected = {
                            selectedHair = it
                            if (message.isNotEmpty()) message = ""
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SimpleDropdown(
                        label = "Skin Type",
                        options = skinOptions,
                        selectedOption = selectedSkin,
                        onOptionSelected = {
                            selectedSkin = it
                            if (message.isNotEmpty()) message = ""
                        }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            if (selectedHair.isBlank() || selectedSkin.isBlank()) {
                                message = "Please select both hair type and skin type"
                                result = ""
                            } else {
                                val hair = hairData[selectedHair]
                                val skin = skinData[selectedSkin]

                                result =
                                    "Hair Care\n" +
                                            "Routine: ${hair?.first}\n" +
                                            "Remedy: ${hair?.second}\n\n" +
                                            "Skin Care\n" +
                                            "Routine: ${skin?.first}\n" +
                                            "Remedy: ${skin?.second}"
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
                            text = "Get My Plan",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }

                    if (message.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
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
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Your Personalized Plan",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5E35B1)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        val parts = result.split("\n\n")
                        if (parts.isNotEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF8F5FF)
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "💇 Hair Care",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF111827)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = parts[0].replace("Hair Care\n", ""),
                                        fontSize = 15.sp,
                                        color = Color(0xFF4B5563)
                                    )
                                }
                            }
                        }

                        if (parts.size > 1) {
                            Spacer(modifier = Modifier.height(14.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFEFFAF3)
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "🧴 Skin Care",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF111827)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = parts[1].replace("Skin Care\n", ""),
                                        fontSize = 15.sp,
                                        color = Color(0xFF4B5563)
                                    )
                                }
                            }
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
                Text(
                    text = "Back to Dashboard",
                    fontSize = 17.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (selectedOption.isEmpty()) "" else selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            placeholder = { Text("Select $label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
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
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun getGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when {
        hour < 12 -> "Good Morning!"
        hour < 17 -> "Good Afternoon!"
        else -> "Good Evening!"
    }
}

fun getTodayDate(): String {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return String.format("%04d-%02d-%02d", year, month, day)
}
fun formatDisplayDate(date: String): String {
    return try {
        val parts = date.split("-")
        if (parts.size != 3) return date

        val monthNames = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        val month = parts[1].toIntOrNull() ?: return date
        val day = parts[2].toIntOrNull() ?: return date

        "${monthNames[month - 1]} $day"
    } catch (e: Exception) {
        date
    }
}
fun getLast7DaysWithZeros(entries: List<StepEntry>): List<StepEntry> {
    val entryMap = entries.associateBy { it.date }
    val calendar = Calendar.getInstance()
    val result = mutableListOf<StepEntry>()

    for (i in 6 downTo 0) {
        val tempCal = calendar.clone() as Calendar
        tempCal.add(Calendar.DAY_OF_MONTH, -i)

        val date = String.format(
            "%04d-%02d-%02d",
            tempCal.get(Calendar.YEAR),
            tempCal.get(Calendar.MONTH) + 1,
            tempCal.get(Calendar.DAY_OF_MONTH)
        )

        result.add(entryMap[date] ?: StepEntry(date = date, steps = 0))
    }

    return result
}

fun formatShortDay(date: String): String {
    return try {
        val parts = date.split("-")
        if (parts.size != 3) return date

        val year = parts[0].toInt()
        val month = parts[1].toInt() - 1
        val day = parts[2].toInt()

        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
        }

        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            else -> "Sat"
        }
    } catch (e: Exception) {
        date
    }
}