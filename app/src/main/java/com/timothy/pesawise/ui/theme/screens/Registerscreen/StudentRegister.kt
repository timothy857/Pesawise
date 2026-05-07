package com.timothy.pesawise.ui.theme.screens.Registerscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timothy.pesawise.navigation.ROUTE_LOGIN
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.timothy.pesawise.models.AccountType
import com.timothy.pesawise.models.THEMES
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timothy.pesawise.viewmodel.AppViewModel
import androidx.compose.ui.platform.LocalContext
import com.timothy.pesawise.data.AuthViewModel
import com.timothy.pesawise.ui.theme.SoftGray
import com.timothy.pesawise.ui.theme.MutedText

@Composable
fun StudentRegisterscreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current
    val authViewModel = remember { AuthViewModel(navController, context) }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    val theme = THEMES[AccountType.Student]!!
    val primaryColor = Color(android.graphics.Color.parseColor(theme.primaryHex))

    val scrollState = rememberScrollState()

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(primaryColor, Color(android.graphics.Color.parseColor(theme.gradientEnd)))
    )

    Box(modifier = modifier.fillMaxSize().background(SoftGray)) {
        // Top Gradient Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .background(brush = gradientBrush)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Profile Image Container
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 12.dp,
                tonalElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = theme.icon,
                        fontSize = 48.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // Main Registration Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = theme.label,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = primaryColor,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Text(
                        text = "Join our community today",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MutedText,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    // Input Fields
                    CustomOutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Full Name",
                        icon = Icons.Default.Person,
                        primaryColor = primaryColor
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    CustomOutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        icon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        primaryColor = primaryColor
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    CustomOutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        icon = Icons.Default.Lock,
                        isPassword = true,
                        primaryColor = primaryColor
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

//                    CustomOutlinedTextField(
//                        value = role,
//                        onValueChange = { role = it },
//                        label = "Choose your role",
//                        icon = Icons.Default.Person,
//                        primaryColor = primaryColor
//                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Sign Up Button
                    Button(
                        onClick = { authViewModel.signup(fullName, email, password, "Student") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text(
                            text = "GET STARTED",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Login Link

                    Text(text = "Already have an account?",
                        color=Color.Black)
                    TextButton(onClick = {
                        navController.navigate(ROUTE_LOGIN)
                    }) {
                        Text(text = "Login", color = Color.Blue)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomOutlinedTextField(
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
                tint = primaryColor
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
            unfocusedContainerColor = Color(0xFFF1F3F4)
        ),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
private fun Registerpreview() {
    StudentRegisterscreen(navController = rememberNavController())
}
