package com.timothy.pesawise.ui.theme.screens.Registerscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.timothy.pesawise.models.AccountType
import com.timothy.pesawise.models.THEMES
import com.timothy.pesawise.navigation.ROUTE_BUSINESS_REGISTER
import com.timothy.pesawise.navigation.ROUTE_STUDENT_REGISTER
import com.timothy.pesawise.navigation.ROUTE_SALARY_REGISTER
import com.timothy.pesawise.ui.theme.RoyalBlue
import com.timothy.pesawise.ui.theme.GradientBlue
import com.timothy.pesawise.ui.theme.SkyBlue
import com.timothy.pesawise.ui.theme.SoftGray

@Composable
fun RegistrationDashboard(navController: NavHostController) {
    val scrollState = rememberScrollState()

    // Themes
    val salaryTheme = THEMES[AccountType.Salaried]!!
    val businessTheme = THEMES[AccountType.Business]!!
    val studentTheme = THEMES[AccountType.Student]!!

    val primaryColor = RoyalBlue
    val secondaryColor = GradientBlue
    val accentColor = SkyBlue
    
    val mainGradient = Brush.linearGradient(
        colors = listOf(primaryColor, secondaryColor, accentColor),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    Box(modifier = Modifier.fillMaxSize().background(SoftGray)) {
        // Decorative Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = secondaryColor.copy(alpha = 0.05f),
                radius = size.width * 0.6f,
                center = Offset(size.width * 0.5f, size.height * 0.9f)
            )
        }

        // Top Gradient Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .background(brush = mainGradient, shape = RoundedCornerShape(bottomStart = 64.dp, bottomEnd = 64.dp))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Branding
            Text(
                text = "PesaWise",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
            )
            
            Text(
                text = "Smart Financial Management",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Light
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Card Container
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
                        text = "Choose Account Type",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1D1D1D)
                        )
                    )
                    Text(
                        text = "Select the option that best describes you",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    // Account Options
                    AccountTypeItem(
                        title = salaryTheme.label,
                        subtitle = "For individuals with monthly income",
                        iconEmoji = salaryTheme.icon,
                        primaryColor = Color(android.graphics.Color.parseColor(salaryTheme.primaryHex)),
                        onClick = { navController.navigate(ROUTE_SALARY_REGISTER) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AccountTypeItem(
                        title = businessTheme.label,
                        subtitle = "For entrepreneurs and companies",
                        iconEmoji = businessTheme.icon,
                        primaryColor = Color(android.graphics.Color.parseColor(businessTheme.primaryHex)),
                        onClick = { navController.navigate(ROUTE_BUSINESS_REGISTER) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AccountTypeItem(
                        title = studentTheme.label,
                        subtitle = "Special features for learners",
                        iconEmoji = studentTheme.icon,
                        primaryColor = Color(android.graphics.Color.parseColor(studentTheme.primaryHex)),
                        onClick = { navController.navigate(ROUTE_STUDENT_REGISTER) }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun AccountTypeItem(
    title: String,
    subtitle: String,
    iconEmoji: String,
    primaryColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = primaryColor.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = iconEmoji,
                        fontSize = 24.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D1D1D)
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    )
                )
            }
            
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationDashboardPreview() {
    RegistrationDashboard(navController = rememberNavController())
}
