package com.timothy.pesawise.ui.theme.screens.HistoryScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timothy.pesawise.models.TransactionType
import com.timothy.pesawise.models.User
import com.timothy.pesawise.ui.components.BackButton
import com.timothy.pesawise.ui.components.GradientHeader
import com.timothy.pesawise.ui.components.TransactionRow
import com.timothy.pesawise.ui.theme.SoftGray

@Composable
fun HistoryScreen(
    user: User,
    accentColor: Color,
    startColor: Color,
    endColor: Color,
    onBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    
    val filteredTransactions = remember(user.transactions, selectedFilter) {
        when (selectedFilter) {
            "Income" -> user.transactions.filter { it.type == TransactionType.Income }
            "Expenses" -> user.transactions.filter { it.type == TransactionType.Expense }
            else -> user.transactions
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftGray)
    ) {
        GradientHeader(startColor, endColor) {
            BackButton(onBack)
            Text(
                "Transaction History",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }

        // Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HistoryFilterChip("All", selectedFilter == "All") { selectedFilter = "All" }
            HistoryFilterChip("Income", selectedFilter == "Income") { selectedFilter = "Income" }
            HistoryFilterChip("Expenses", selectedFilter == "Expenses") { selectedFilter = "Expenses" }
        }

        if (filteredTransactions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📜", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "No transactions found",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                items(filteredTransactions) { transaction ->
                    TransactionRow(transaction, accentColor)
                }
                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
fun HistoryFilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color.Black else Color.White,
        tonalElevation = 2.dp,
        modifier = Modifier.height(40.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = if (isSelected) Color.White else Color.Gray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
