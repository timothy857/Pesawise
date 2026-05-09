package com.timothy.pesawise.ui.theme.screens.Maindashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timothy.pesawise.models.User
import com.timothy.pesawise.ui.components.*
import com.timothy.pesawise.ui.theme.DangerRed
import com.timothy.pesawise.ui.theme.MutedGray
import com.timothy.pesawise.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

// ─────────────────────────────────────────────────────────
//  HISTORY SCREEN
// ─────────────────────────────────────────────────────────
@Composable
fun HistoryScreen(user: User, accentColor: Color, startColor: Color, endColor: Color, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF4F7F5))) {
        GradientHeader(startColor, endColor) {
            BackButton(onBack)
            Text("📜 Transaction History", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp)) {
            if (user.transactions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(top = 100.dp), contentAlignment = Alignment.Center) {
                    Text("No transactions yet.", color = MutedGray)
                }
            } else {
                user.transactions.forEach { TransactionRow(it, accentColor) }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
//  PROFILE SCREEN
// ─────────────────────────────────────────────────────────
@Composable
fun ProfileScreen(user: User, vm: AppViewModel, accentColor: Color, startColor: Color, endColor: Color, onBack: () -> Unit, onLogout: () -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF4F7F5))) {
        Box(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(startColor, endColor)))) {
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    BackButton(onBack)
                    Text(
                        text = if (isEditing) "Cancel" else "Edit",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            if (isEditing) {
                                // Reset fields on cancel
                                name = user.name
                                email = user.email
                                phone = user.phone
                            }
                            isEditing = !isEditing
                        }
                    )
                }
                Surface(modifier = Modifier.size(80.dp), shape = RoundedCornerShape(25.dp), color = Color.White.copy(alpha = 0.2f)) {
                    Box(contentAlignment = Alignment.Center) { Text(name.take(1), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black) }
                }
                Spacer(Modifier.height(12.dp))
                Text(name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                Text(email, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isEditing) {
                PesaCard {
                    Text("Edit Profile", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = accentColor)
                    Spacer(Modifier.height(16.dp))
                    PesaInput(value = name, onValueChange = { name = it }, label = "Full Name")
                    Spacer(Modifier.height(12.dp))
                    PesaInput(value = email, onValueChange = { email = it }, label = "Email Address", keyboardType = androidx.compose.ui.text.input.KeyboardType.Email)
                    Spacer(Modifier.height(12.dp))
                    PesaInput(value = phone, onValueChange = { phone = it }, label = "Phone Number", keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone)
                    Spacer(Modifier.height(24.dp))
                    PesaButton(label = "Save Changes", color = accentColor) {
                        vm.updateProfile(name, email, phone)
                        isEditing = false
                    }
                }
            } else {
                PesaCard {
                    Text("Account Details", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(Modifier.height(12.dp))
                    ProfileRow("Account Type", user.type.name)
                    ProfileRow("Phone", user.phone)
                    ProfileRow("Email", user.email)
                }
            }

            Spacer(Modifier.weight(1f))
            PesaOutlineButton("Logout", DangerRed, onLogout)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MutedGray, fontSize = 13.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}
