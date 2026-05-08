package com.timothy.pesawise.ui.theme.screens.Maindashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timothy.pesawise.models.User
import com.timothy.pesawise.ui.components.*
import com.timothy.pesawise.viewmodel.AppViewModel

@Composable
fun AddIncomeScreen(user: User, vm: AppViewModel, accentColor: Color, onBack: () -> Unit, onSaved: () -> Unit) {
    var amount   by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Salary") }
    var note     by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        GradientHeader(accentColor, accentColor.copy(alpha = 0.8f)) {
            BackButton(onBack)
            Text("Add Income", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White)
            Spacer(Modifier.height(18.dp))
            Text("AMOUNT (KES)", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                textStyle = LocalTextStyle.current.copy(fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color.White),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    cursorColor = Color.White
                )
            )
        }

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            PesaInput(category, { category = it }, "Source", "e.g. Freelance, Gift")
            PesaInput(note,     { note = it },     "Note",   "Additional details")

            Spacer(Modifier.height(10.dp))
            PesaButton("💰 Save Income", color = accentColor, onClick = {
                val amt = amount.toDoubleOrNull()
                if (amt != null && amt > 0) {
                    vm.addIncome(amt, category, note, "💰")
                    onSaved()
                }
            })
        }
    }
}
