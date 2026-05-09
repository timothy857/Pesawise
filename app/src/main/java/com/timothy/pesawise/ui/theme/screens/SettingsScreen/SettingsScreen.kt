package com.timothy.pesawise.ui.theme.screens.SettingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.timothy.pesawise.models.User
import com.timothy.pesawise.ui.components.*
import com.timothy.pesawise.ui.theme.MutedGray
import com.timothy.pesawise.ui.theme.SoftGray

@Composable
fun SettingsScreen(
    user: User,
    accentColor: Color,
    startColor: Color,
    endColor: Color,
    onBack: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var biometricsEnabled by remember { mutableStateOf(false) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftGray)
    ) {
        GradientHeader(startColor, endColor) {
            BackButton(onBack)
            Text(
                "Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // General Settings
            SettingsSection(title = "General") {
                SettingsToggleRow(
                    icon = "🔔",
                    label = "Notifications",
                    description = "Get alerts for budgets and goals",
                    checked = notificationsEnabled,
                    color = accentColor,
                    onCheckedChange = { notificationsEnabled = it }
                )
                HorizontalDivider(color = SoftGray, modifier = Modifier.padding(vertical = 8.dp))
                SettingsToggleRow(
                    icon = "🌙",
                    label = "Dark Mode",
                    description = "Change app appearance",
                    checked = darkModeEnabled,
                    color = accentColor,
                    onCheckedChange = { darkModeEnabled = it }
                )
            }

            // Security Settings
            SettingsSection(title = "Security") {
                SettingsToggleRow(
                    icon = "🛡️",
                    label = "Biometric Lock",
                    description = "Use fingerprint to open PesaWise",
                    checked = biometricsEnabled,
                    color = accentColor,
                    onCheckedChange = { biometricsEnabled = it }
                )
                HorizontalDivider(color = SoftGray, modifier = Modifier.padding(vertical = 8.dp))
                SettingsClickRow(
                    icon = "🔑",
                    label = "Change Password",
                    onClick = { /* Handle navigation */ }
                )
            }

            // Support & About
            SettingsSection(title = "Support") {
                SettingsClickRow(
                    icon = "❓",
                    label = "Help & Support",
                    onClick = { }
                )
                HorizontalDivider(color = SoftGray, modifier = Modifier.padding(vertical = 8.dp))
                SettingsClickRow(
                    icon = "📜",
                    label = "Terms & Privacy Policy",
                    onClick = { }
                )
                HorizontalDivider(color = SoftGray, modifier = Modifier.padding(vertical = 8.dp))
                SettingsClickRow(
                    icon = "ℹ️",
                    label = "About PesaWise",
                    description = "Version 1.0.0",
                    onClick = { }
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MutedGray,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        PesaCard(content = content)
    }
}

@Composable
fun SettingsToggleRow(
    icon: String,
    label: String,
    description: String? = null,
    checked: Boolean,
    color: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(icon, fontSize = 20.sp)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                if (description != null) {
                    Text(description, color = MutedGray, fontSize = 11.sp)
                }
            }
        }
        PesaToggle(on = checked, color = color, onChange = onCheckedChange)
    }
}

@Composable
fun SettingsClickRow(
    icon: String,
    label: String,
    description: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 20.sp)
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            if (description != null) {
                Text(description, color = MutedGray, fontSize = 11.sp)
            }
        }
        Text("→", color = MutedGray, fontSize = 16.sp)
    }
}
