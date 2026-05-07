package com.timothy.pesawise.ui.theme.screens.Registerscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.timothy.pesawise.models.AccountType
import com.timothy.pesawise.models.THEMES
import com.timothy.pesawise.navigation.ROUTE_LOGIN
import com.timothy.pesawise.navigation.ROUTE_SALARY_DASHBOARD
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timothy.pesawise.viewmodel.AppViewModel
import androidx.compose.ui.platform.LocalContext
import com.timothy.pesawise.data.AuthViewModel
import com.timothy.pesawise.ui.theme.SoftGray
import com.timothy.pesawise.ui.theme.MutedText

@Composable
fun SalaryRegisterScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current
    val authViewModel = remember { AuthViewModel(navController, context) }
    var fullName by remember { mutableStateOf("") }
    var personalEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var professionName by remember { mutableStateOf("") }

    val theme = THEMES[AccountType.Salaried]!!
    val primaryColor = Color(android.graphics.Color.parseColor(theme.primaryHex))
    val accentColor = Color(android.graphics.Color.parseColor(theme.accentHex))

    val scrollState = rememberScrollState()

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(primaryColor, Color(android.graphics.Color.parseColor(theme.gradientEnd)))
    )

    Box(modifier = Modifier.fillMaxSize().background(SoftGray)) {
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

            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 12.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = theme.icon,
                        fontSize = 48.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

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
                            color = primaryColor
                        )
                    )
                    Text(
                        text = "Manage your monthly income",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MutedText)
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    CustomOutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Full Name",
                        icon = Icons.Default.Person,
                        primaryColor = primaryColor
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    CustomOutlinedTextField(
                        value = personalEmail,
                        onValueChange = { personalEmail = it },
                        label = "Personal Email",
                        icon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        primaryColor = primaryColor
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    CustomOutlinedTextField(
                        value = professionName,
                        onValueChange = { professionName = it },
                        label = "Profession Name",
                        icon = Icons.Default.Home,
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

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = { authViewModel.signup(fullName, personalEmail, password, "Salaried") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        Text(
                            text = "CREATE ACCOUNT",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "Already have an account?", color = Color.Black)
                    TextButton(onClick = { navController.navigate(ROUTE_LOGIN) }) {
                        Text(text = "Login", color = primaryColor)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SalaryRegisterPreview() {
    SalaryRegisterScreen(navController = rememberNavController())
}
