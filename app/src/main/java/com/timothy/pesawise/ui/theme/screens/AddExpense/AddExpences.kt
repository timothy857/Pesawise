package com.timothy.pesawise.ui.theme.screens.AddExpense

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.timothy.pesawise.models.Category
import com.timothy.pesawise.models.EXPENSE_CATEGORIES
import com.timothy.pesawise.ui.components.BackButton
import com.timothy.pesawise.ui.components.PesaButton
import com.timothy.pesawise.ui.components.PesaInput
import com.timothy.pesawise.ui.theme.DangerRed
import com.timothy.pesawise.ui.theme.MutedGray
import com.timothy.pesawise.ui.theme.SoftGray
import com.timothy.pesawise.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddExpencesScreen(navController: NavHostController, viewModel: AppViewModel) {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Food") }
    var date by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())) }

    val pinkColor = DangerRed

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftGray)
    ) {
        // Pink Header Section with Rounded Bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    pinkColor,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(bottom = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp)
            ) {
                BackButton(onClick = { navController.popBackStack() })
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Add Expense",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "AMOUNT (KES)",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = { 
                        Text("0", color = Color.White.copy(alpha = 0.4f), fontSize = 48.sp, fontWeight = FontWeight.Black) 
                    },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedContainerColor = Color.White.copy(alpha = 0.15f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.15f),
                        cursorColor = Color.White
                    ),
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true
                )
            }
        }

        // Content Section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Category Section
            Column {
                Text(
                    text = "CATEGORY",
                    color = MutedGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                val rows = EXPENSE_CATEGORIES.chunked(4)
                rows.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { item ->
                            Box(modifier = Modifier.weight(1f)) {
                                LocalCategoryCard(
                                    item = item,
                                    isSelected = selectedCategory == item.name,
                                    onClick = { selectedCategory = item.name }
                                )
                            }
                        }
                        repeat(4 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Note Section
            PesaInput(
                value = note,
                onValueChange = { note = it },
                label = "Note",
                placeholder = "What was this for?"
            )

            // Date Section
            Column {
                Text(
                    text = "DATE",
                    color = MutedGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    readOnly = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            PesaButton(
                label = "💸 Save Expense",
                color = pinkColor,
                onClick = {
                    val amt = amount.toDoubleOrNull()
                    if (amt != null && amt > 0) {
                        val icon = EXPENSE_CATEGORIES.find { it.name == selectedCategory }?.icon ?: "🗂️"
                        viewModel.addExpense(amt, selectedCategory, note, date, icon)
                        navController.popBackStack()
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LocalCategoryCard(item: Category, isSelected: Boolean, onClick: () -> Unit) {
    val accentColor = DangerRed
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Card(
            modifier = Modifier.aspectRatio(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) accentColor.copy(alpha = 0.1f) else Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 0.dp else 2.dp),
            border = if (isSelected) BorderStroke(1.5.dp, accentColor) else null
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = item.icon, fontSize = 24.sp)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.name,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) accentColor else MutedGray,
            textAlign = TextAlign.Center
        )
    }
}
