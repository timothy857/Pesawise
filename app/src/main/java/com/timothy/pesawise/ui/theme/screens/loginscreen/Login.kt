package com.timothy.pesawise.ui.theme.screens.loginscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timothy.pesawise.navigation.ROUTE_REG_DASHBOARD
import com.timothy.pesawise.navigation.ROUTE_SALARY_DASHBOARD
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import androidx.lifecycle.viewmodel.compose.viewModel
import com.timothy.pesawise.viewmodel.AppViewModel
import com.timothy.pesawise.models.AccountType
import androidx.compose.ui.platform.LocalContext
import com.timothy.pesawise.data.AuthViewModel
import com.timothy.pesawise.navigation.ROUTE_REG_DASHBOARD
import com.timothy.pesawise.ui.theme.*

@Composable
fun LoginScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current
    val authViewModel = remember { AuthViewModel(navController, context) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Professional color palette (matching branding)
    val primaryColor = RoyalBlue
    val secondaryColor = GradientBlue
    val accentColor = SkyBlue
    
    val mainGradient = Brush.linearGradient(
        colors = listOf(primaryColor, secondaryColor, accentColor),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    Box(modifier = modifier.fillMaxSize().background(SoftGray)) {
        // Background Decorative Elements
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = secondaryColor.copy(alpha = 0.05f),
                radius = size.width * 0.4f,
                center = Offset(size.width * 0.1f, size.height * 0.9f)
            )
        }

        // Top Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.38f)
                .background(brush = mainGradient, shape = RoundedCornerShape(bottomEnd = 64.dp))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // App/Screen Title
            Text(
                text = "PesaWise",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
            )
            
            Spacer(modifier = Modifier.height(20.dp))

            // Login Icon with ring
            Box(contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.size(105.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f)
                ) {}
                Surface(
                    modifier = Modifier.size(85.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 16.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Login",
                            modifier = Modifier.size(44.dp),
                            tint = primaryColor
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(30.dp))

            // Main Login Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome Back",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                    )
                    Text(
                        text = "Login to your account",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MutedText
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    if (loginError) {
                        Text(
                            text = "Invalid email or password",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Input Fields
                    LoginCustomTextField(
                        value = email,
                        onValueChange = { email = it; loginError = false },
                        label = "Email Address",
                        icon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        primaryColor = primaryColor
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    LoginCustomTextField(
                        value = password,
                        onValueChange = { password = it; loginError = false },
                        label = "Password",
                        icon = Icons.Default.Lock,
                        isPassword = true,
                        primaryColor = primaryColor
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Forgot Password Link
                    TextButton(
                        onClick = { /* Handle Forgot Password */ },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "Forgot Password?",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = primaryColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Login Button with Gradient
                    Button(
                        onClick = { authViewModel.login(email.trim(), password.trim()) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(brush = mainGradient, shape = RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "LOGIN",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Register Link
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Don't have an account?",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                        TextButton(
                            onClick = { navController.navigate(ROUTE_REG_DASHBOARD) }
                        ) {
                            Text(
                                text = "Sign Up",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = primaryColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginCustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    primaryColor: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(22.dp)
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = primaryColor,
            focusedLabelColor = primaryColor,
            cursorColor = primaryColor,
            unfocusedBorderColor = Color(0xFFE0E0E0),
            unfocusedContainerColor = SoftGray,
            unfocusedLabelColor = MutedText,
            focusedContainerColor = Color.White
        ),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginPrev() {
    LoginScreen(navController = rememberNavController())
}
