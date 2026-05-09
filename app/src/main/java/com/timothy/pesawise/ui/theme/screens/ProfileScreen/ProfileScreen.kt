package com.timothy.pesawise.ui.theme.screens.ProfileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timothy.pesawise.models.User
import com.timothy.pesawise.ui.components.*
import com.timothy.pesawise.ui.theme.DangerRed
import com.timothy.pesawise.ui.theme.MutedGray
import com.timothy.pesawise.ui.theme.SoftGray
import com.timothy.pesawise.viewmodel.AppViewModel

@Composable
fun ProfileScreen(
    user: User,
    vm: AppViewModel,
    accentColor: Color,
    startColor: Color,
    endColor: Color,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onSettings: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var fullname by remember { mutableStateOf(user.fullname) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftGray)
    ) {
        GradientHeader(startColor, endColor) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(onBack)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                    }
                    Text(
                        text = if (isEditing) "Cancel" else "Edit",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            if (isEditing) {
                                fullname = user.fullname
                                email = user.email
                                phone = user.phone
                            }
                            isEditing = !isEditing
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (fullname.isNotEmpty()) fullname.take(1).uppercase() else "P",
                            color = Color.White,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    text = user.fullname,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = user.email,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (isEditing) {
                PesaCard {
                    Text(
                        "EDIT PROFILE DETAILS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = accentColor,
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(20.dp))
                    
                    PesaInput(
                        value = fullname,
                        onValueChange = { fullname = it },
                        label = "Full Name",
                        placeholder = "Enter your name"
                    )
                    Spacer(Modifier.height(16.dp))
                    
                    PesaInput(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        placeholder = "email@example.com",
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
                    )
                    Spacer(Modifier.height(16.dp))
                    
                    PesaInput(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Phone Number",
                        placeholder = "0712345678",
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone
                    )
                    
                    Spacer(Modifier.height(32.dp))
                    
                    PesaButton(
                        label = "Update Profile",
                        color = accentColor
                    ) {
                        vm.updateProfile(fullname, email, phone)
                        isEditing = false
                    }
                }
            } else {
                PesaCard {
                    Text(
                        "ACCOUNT INFORMATION",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MutedGray,
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    
                    ProfileInfoRow(icon = "👤", label = "Full Name", value = user.fullname)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SoftGray)
                    ProfileInfoRow(icon = "📧", label = "Email", value = user.email)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SoftGray)
                    ProfileInfoRow(icon = "📞", label = "Phone", value = if (user.phone.isEmpty()) "Not set" else user.phone)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SoftGray)
                    ProfileInfoRow(icon = "💼", label = "Account Type", value = user.type.name)
                }
                
                PesaCard {
                    Text(
                        "FINANCIAL OVERVIEW",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MutedGray,
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Balance", color = MutedGray, fontSize = 12.sp)
                            Text(money(user.balance), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Total Savings", color = MutedGray, fontSize = 12.sp)
                            Text(money(user.totalSavings), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF00C896))
                        }
                    }
                }

                PesaCard(onClick = onSettings) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚙️", fontSize = 18.sp)
                        Spacer(Modifier.width(16.dp))
                        Text("App Settings", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Text("→", color = MutedGray)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            
            PesaOutlineButton(
                label = "Logout",
                color = DangerRed
            ) {
                onLogout()
            }
            
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProfileInfoRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(SoftGray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 18.sp)
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(label, color = MutedGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}
