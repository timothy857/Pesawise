package com.timothy.pesawise.ui.theme.screens.Maindashboard

import androidx.compose.foundation.background
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
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF4F7F5))) {
        Box(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(startColor, endColor)))) {
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.fillMaxWidth()) { BackButton(onBack) }
                Surface(modifier = Modifier.size(80.dp), shape = RoundedCornerShape(25.dp), color = Color.White.copy(alpha = 0.2f)) {
                    Box(contentAlignment = Alignment.Center) { Text(user.name.take(1), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black) }
                }
                Spacer(Modifier.height(12.dp))
                Text(user.name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                Text(user.email, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
            }
        }
        Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PesaCard {
                Text("Account Details", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))
                ProfileRow("Account Type", user.type.name)
                ProfileRow("Phone", user.phone)
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
