package com.example.gogym.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gogym.AuthState
import com.example.gogym.AuthViewModel

@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }   // 👁

    val bgColor = Color(0xFFF1F1F1)
    val primaryRed = Color(0xFFFF4B4B)
    val cardColor = Color.White
    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
            else -> Unit
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.size(90.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏋️", fontSize = 32.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Go ",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Gym",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryRed
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Sign In",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Email") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🔐 Password có icon mắt
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Password") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Filled.VisibilityOff
                            else
                                Icons.Filled.Visibility,
                            contentDescription = if (passwordVisible)
                                "Hide password" else "Show password"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.login(email.trim(), password.trim())
                },
                enabled = authState.value !is AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryRed
                )
            ) {
                Text(text = "Sign In", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Forgot Password?",
                fontSize = 12.sp,
                modifier = Modifier.clickable { },
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Sign in with",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, shape = CircleShape)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Text("G", fontSize = 24.sp, color = Color(0xFF4285F4))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Don't have an account? ", fontSize = 14.sp)
                Text(
                    text = "Sign Up",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryRed,
                    modifier = Modifier.clickable {
                        navController.navigate("signup")
                    }
                )
            }
        }
    }
}
