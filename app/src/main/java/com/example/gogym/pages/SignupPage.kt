package com.example.gogym.pages
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gogym.AuthState
import com.example.gogym.AuthViewModel

@Composable
fun SignupPage(modifier: Modifier = Modifier, navController: NavHostController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    var authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit



        }

    }


    val bgColor = Color(0xFFF1F1F1)          // màu xám nền
    val primaryRed = Color(0xFFFF4B4B)       // màu nút Sign In
    val cardColor = Color.White

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

            // logo icon (ô vuông viền tròn)
            Card(
                modifier = Modifier.size(90.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                // TODO: đặt Image logo tạ/địa điểm vào đây
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏋️", fontSize = 32.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // chữ Go Gym
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

            // TextField Email/Phone
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(" Email") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // TextField Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Password") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
                // TODO: thêm visualTransformation cho ẩn mật khẩu nếu muốn
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nút Sign In (email/password)
            Button(
                onClick = {
                    authViewModel.signup(email, password)
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
                Text(text = "Creat Account", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Forgot Password?",
                fontSize = 12.sp,
                modifier = Modifier.clickable {
                    // TODO: điều hướng sang màn reset password
                },
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Sign in with",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nút Google tròn
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, shape = CircleShape)
                    .clickable {
                        // TODO: gọi hàm login Google ở MainActivity/AuthViewModel
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("G", fontSize = 24.sp, color = Color(0xFF4285F4)) // tạm, sau thay bằng logo Google
                // Khi có file logo, dùng Image(painterResource(R.drawable.ic_google), contentDescription = "Google")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // dòng "Don't have an account? Sign Up"
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Already have an account, ", fontSize = 14.sp)
                Text(
                    text = "Login",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryRed,
                    modifier = Modifier.clickable {
                        navController.navigate("login")
                    }
                )
            }
        }
    }
}
