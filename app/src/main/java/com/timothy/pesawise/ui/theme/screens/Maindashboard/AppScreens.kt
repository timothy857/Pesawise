package com.timothy.pesawise.ui.theme.screens.Maindashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timothy.pesawise.models.AccountType
import com.timothy.pesawise.models.EXPENSE_CATEGORIES
import com.timothy.pesawise.models.PIE_COLORS
import com.timothy.pesawise.models.THEMES
import com.timothy.pesawise.models.TransactionType
import com.timothy.pesawise.models.User
import com.timothy.pesawise.ui.components.*
import com.timothy.pesawise.ui.theme.*
import com.timothy.pesawise.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

// ─────────────────────────────────────────────────────────
//  ADD EXPENSE SCREEN
// ─────────────────────────────────────────────────────────
@Composable
fun AddExpenseScreen(vm: AppViewModel, onBack: () -> Unit, onSaved: () -> Unit) {
    var amount   by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note     by remember { mutableStateOf("") }
    var date     by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Red header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DangerRed)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                BackButton(onBack)
                Text("Add Expense", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White)
                Spacer(Modifier.height(18.dp))
                Text("AMOUNT (KES)", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                Spacer(Modifier.height(8.dp))
                
                // Transparent, large text field for amount
                OutlinedTextField(
                    value = amount,
                    onValueChange = { if (it.length <= 10) amount = it },
                    placeholder = { Text("0", color = Color.White.copy(alpha = 0.4f), fontSize = 42.sp, fontWeight = FontWeight.Black) },
                    textStyle = TextStyle(fontSize = 42.sp, fontWeight = FontWeight.Black, color = Color.White),
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    shape  = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = Color.White.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedContainerColor   = Color.White.copy(alpha = 0.12f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.12f),
                        cursorColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("CATEGORY", style = LocalTextStyle.current.copy(fontSize = 12.sp, fontWeight = FontWeight.Black, color = MutedGray, letterSpacing = 0.8.sp))
            
            // Category grid
            val rows = EXPENSE_CATEGORIES.chunked(4)
            rows.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { cat ->
                        val selected = category == cat.name
                        Card(
                            onClick  = { category = cat.name },
                            modifier = Modifier.weight(1f),
                            shape    = RoundedCornerShape(16.dp),
                            border   = BorderStroke(1.dp, if (selected) DangerRed else Color(0xFFF1F5F9)),
                            colors   = CardDefaults.cardColors(containerColor = if (selected) DangerRed.copy(alpha = 0.05f) else Color.White),
                            elevation= CardDefaults.cardElevation(if (selected) 2.dp else 0.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(cat.icon, fontSize = 24.sp)
                                Spacer(Modifier.height(4.dp))
                                Text(cat.name, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                                    color = if (selected) DangerRed else MutedGray)
                            }
                        }
                    }
                    repeat(4 - row.size) { Box(Modifier.weight(1f)) }
                }
            }

            PesaInput(note, { note = it }, "Note", "What was this for?")
            PesaInput(date, { date = it }, "Date", "dd/mm/yyyy")

            Spacer(Modifier.height(8.dp))
            PesaButton("💸 Save Expense", color = DangerRed, onClick = {
                val amt = amount.toDoubleOrNull()
                if (amt != null && amt > 0 && category.isNotEmpty()) {
                    val icon = EXPENSE_CATEGORIES.find { it.name == category }?.icon ?: "🗂️"
                    vm.addExpense(amt, category, note, date, icon)
                    onSaved()
                }
            })
            Spacer(Modifier.height(24.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────
//  ADD INCOME SCREEN
// ─────────────────────────────────────────────────────────
@Composable
fun AddIncomeScreen(user: User, vm: AppViewModel, accentColor: Color, onBack: () -> Unit, onSaved: () -> Unit) {
    var amount by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("") }
    var note   by remember { mutableStateOf("") }

    val sources = when (user.type) {
        AccountType.Business -> listOf("Shop Sales","Wholesale Order","Online Sales","Services","Rental Income","Other")
        AccountType.Student  -> listOf("Allowance","HELB","Part-Time Job","Freelance","Gift","Other")
        else                 -> listOf("Salary","Side Hustle","Freelance","Investment","Business Dividend","Other")
    }
    val incIcon = when (user.type) { AccountType.Business -> "🛒"; AccountType.Student -> "🎓"; else -> "💼" }
    val headerLabel = if (user.type == AccountType.Business) "Record a Sale" else "Add Income"

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().background(accentColor)) {
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(20.dp)) {
                BackButton(onBack)
                Text(headerLabel, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(Modifier.height(14.dp))
                Text("AMOUNT (KES)", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = amount, onValueChange = { amount = it },
                    placeholder = { Text("0", color = Color.White.copy(alpha = 0.5f), fontSize = 28.sp, fontWeight = FontWeight.Black) },
                    textStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White),
                    modifier = Modifier.fillMaxWidth(),
                    shape  = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = Color.White.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedContainerColor   = Color.White.copy(alpha = 0.15f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.15f)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("SOURCE", style = LocalTextStyle.current.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MutedGray, letterSpacing = 0.8.sp))
            sources.forEach { s ->
                val sel = source == s
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { source = s },
                    shape    = RoundedCornerShape(12.dp),
                    border   = BorderStroke(2.dp, if (sel) accentColor else Color(0xFFE8EFED)),
                    colors   = CardDefaults.cardColors(containerColor = if (sel) accentColor.copy(alpha = 0.08f) else Color.White),
                    elevation= CardDefaults.cardElevation(2.dp)
                ) {
                    Text(s, modifier = Modifier.padding(14.dp), fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp, color = if (sel) Color(0xFF0D3B2E) else Color(0xFF111111))
                }
            }

            Spacer(Modifier.height(4.dp))
            PesaInput(note, { note = it }, "Note (optional)", "e.g. April salary from KCB")
            Spacer(Modifier.height(4.dp))

            PesaButton("$incIcon Save Income", color = accentColor, onClick = {
                val amt = amount.toDoubleOrNull()
                if (amt != null && amt > 0 && source.isNotEmpty()) {
                    vm.addIncome(amt, source, note, incIcon)
                    onSaved()
                }
            })
        }
    }
}

// ─────────────────────────────────────────────────────────
//  REPORTS SCREEN
// ─────────────────────────────────────────────────────────
@Composable
fun ReportsScreen(user: User, accentColor: Color, startColor: Color, endColor: Color, onBack: () -> Unit) {
    var tab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Overview", "Monthly", "Breakdown")

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(startColor, endColor)))) {
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(20.dp)) {
                BackButton(onBack)
                Text("📊 Reports & Analytics", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    tabs.forEachIndexed { i, label ->
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                .background(if (tab == i) accentColor else Color.White.copy(alpha = 0.15f))
                                .clickable { tab = i }.padding(horizontal = 14.dp, vertical = 7.dp)
                        ) { Text(label, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (tab) {
                0 -> {
                    // Overview stats
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        listOf(
                            Triple("📈", "Total Income",  Pair(money(user.income),  accentColor)),
                            Triple("📉", "Total Expense", Pair(money(user.expenses), DangerRed))
                        ).forEach { (icon, label, vp) ->
                            Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                                Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(icon, fontSize = 24.sp)
                                    Text(vp.first, fontWeight = FontWeight.Black, fontSize = 14.sp, color = vp.second)
                                    Text(label, fontSize = 10.sp, color = MutedGray)
                                }
                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        val savings = user.income - user.expenses
                        val rate    = if (user.income > 0) ((savings / user.income) * 100).toInt() else 0
                        listOf(
                            Triple("💰", "Net Savings",  Pair(money(savings), Color(0xFF4ECDC4))),
                            Triple("🎯", "Savings Rate", Pair("$rate%",       Color(0xFFF5C842)))
                        ).forEach { (icon, label, vp) ->
                            Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                                Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(icon, fontSize = 24.sp)
                                    Text(vp.first, fontWeight = FontWeight.Black, fontSize = 14.sp, color = vp.second)
                                    Text(label, fontSize = 10.sp, color = MutedGray)
                                }
                            }
                        }
                    }
                    // Pie chart placeholder
                    if (user.pieData.isNotEmpty()) {
                        PesaCard {
                            Text("Spending Breakdown", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                            Spacer(Modifier.height(12.dp))
                            user.pieData.forEachIndexed { i, entry ->
                                val color = Color(PIE_COLORS[i % PIE_COLORS.size])
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(99.dp)).background(color))
                                    Text(entry.name, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                    Text(money(entry.value), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DangerRed)
                                }
                                PesaProgressBar(entry.value, user.expenses.coerceAtLeast(1.0), color)
                                Spacer(Modifier.height(6.dp))
                            }
                        }
                    }
                }
                1 -> {
                    // Monthly bar visualization
                    PesaCard {
                        Text("Income vs Expenses (6 months)", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        Spacer(Modifier.height(14.dp))
                        if (user.monthly.isNotEmpty()) {
                            val maxVal = user.monthly.maxOf { maxOf(it.income, it.expense) }.coerceAtLeast(1.0)
                            user.monthly.forEach { m ->
                                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                                    Text(m.month, fontSize = 11.sp, color = MutedGray, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("In", fontSize = 10.sp, color = accentColor, modifier = Modifier.width(20.dp))
                                        PesaProgressBar(m.income, maxVal, accentColor, modifier = Modifier.weight(1f))
                                        Text(money(m.income), fontSize = 10.sp, color = accentColor, modifier = Modifier.width(70.dp))
                                    }
                                    Spacer(Modifier.height(3.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("Out", fontSize = 10.sp, color = DangerRed, modifier = Modifier.width(20.dp))
                                        PesaProgressBar(m.expense, maxVal, DangerRed, modifier = Modifier.weight(1f))
                                        Text(money(m.expense), fontSize = 10.sp, color = DangerRed, modifier = Modifier.width(70.dp))
                                    }
                                }
                            }
                        } else {
                            Text("No monthly data yet.", fontSize = 13.sp, color = MutedGray)
                        }
                    }
                }
                2 -> {
                    if (user.pieData.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                            Text("Add some expenses to see breakdown.", fontSize = 13.sp, color = MutedGray)
                        }
                    } else {
                        user.pieData.forEachIndexed { i, entry ->
                            val color = Color(PIE_COLORS[i % PIE_COLORS.size])
                            PesaCard {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(entry.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(money(entry.value), fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = DangerRed)
                                }
                                Spacer(Modifier.height(8.dp))
                                PesaProgressBar(entry.value, user.expenses.coerceAtLeast(1.0), color)
                                Spacer(Modifier.height(5.dp))
                                Text("${((entry.value / user.expenses.coerceAtLeast(1.0)) * 100).toInt()}% of total",
                                    fontSize = 11.sp, color = MutedGray)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
//  GOALS SCREEN
// ─────────────────────────────────────────────────────────
@Composable
fun GoalsScreen(user: User, accentColor: Color, startColor: Color, endColor: Color, onBack: () -> Unit) {
    var showForm by remember { mutableStateOf(false) }
    var newTitle  by remember { mutableStateOf("") }
    var newTarget by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(startColor, endColor)))) {
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(20.dp)) {
                BackButton(onBack)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("🎯 Savings Goals", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                            .background(accentColor).clickable { showForm = !showForm }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) { Text("+ New", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (showForm) {
                Card(
                    shape  = RoundedCornerShape(16.dp),
                    border = BorderStroke(2.dp, accentColor),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("✨ New Savings Goal", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        PesaInput(newTitle,  { newTitle  = it }, "Goal Name",              "e.g. Buy Laptop")
                        PesaInput(newTarget, { newTarget = it }, "Target Amount (KES)",    "60000", keyboardType = KeyboardType.Number)
                        PesaButton("🚀 Create Goal", color = accentColor, onClick = { /* TODO: persist */ })
                    }
                }
            }

            if (user.goals.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎯", fontSize = 52.sp)
                        Spacer(Modifier.height(12.dp))
                        Text("No goals yet", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Tap \"+ New\" to add your first goal!", fontSize = 13.sp, color = MutedGray)
                    }
                }
            } else {
                user.goals.forEach { g ->
                    val pct  = ((g.saved / g.target) * 100).toInt().coerceIn(0, 100)
                    val gCol = Color(android.graphics.Color.parseColor(g.colorHex))
                    val chipColor = when {
                        pct >= 75 -> accentColor
                        pct >= 40 -> Color(0xFFF5C842)
                        else      -> DangerRed
                    }
                    PesaCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(13.dp)).background(gCol.copy(alpha = 0.13f)),
                                    contentAlignment = Alignment.Center
                                ) { Text(g.icon, fontSize = 22.sp) }
                                Column {
                                    Text(g.title, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                                    Text("Due: ${g.deadline}", fontSize = 11.sp, color = MutedGray)
                                }
                            }
                            PesaChip("$pct%", chipColor)
                        }
                        Spacer(Modifier.height(12.dp))
                        PesaProgressBar(g.saved, g.target, gCol)
                        Spacer(Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${money(g.saved)} saved", fontSize = 11.sp, color = accentColor, fontWeight = FontWeight.Bold)
                            Text("${money(g.target - g.saved)} to go", fontSize = 11.sp, color = MutedGray)
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick  = {},
                                modifier = Modifier.weight(1f).height(36.dp),
                                shape    = RoundedCornerShape(10.dp),
                                colors   = ButtonDefaults.buttonColors(containerColor = gCol.copy(alpha = 0.13f), contentColor = gCol)
                            ) { Text("💵 Add Savings", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                            Button(
                                onClick  = {},
                                modifier = Modifier.weight(1f).height(36.dp),
                                shape    = RoundedCornerShape(10.dp),
                                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F7F5), contentColor = MutedGray)
                            ) { Text("📊 Details", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
//  HISTORY SCREEN
// ─────────────────────────────────────────────────────────
@Composable
fun HistoryScreen(user: User, accentColor: Color, startColor: Color, endColor: Color, onBack: () -> Unit) {
    var filter by remember { mutableIntStateOf(0) } // 0=all 1=income 2=expense
    val labels = listOf("All", "Income", "Expense")
    val filtered = when (filter) {
        1    -> user.transactions.filter { it.type == TransactionType.Income }
        2    -> user.transactions.filter { it.type == TransactionType.Expense }
        else -> user.transactions
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(startColor, endColor)))) {
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(20.dp)) {
                BackButton(onBack)
                Text("📋 All Transactions", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    labels.forEachIndexed { i, label ->
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                .background(if (filter == i) accentColor else Color.White.copy(alpha = 0.15f))
                                .clickable { filter = i }.padding(horizontal = 14.dp, vertical = 7.dp)
                        ) { Text(label, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }

        if (filtered.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No transactions found.", fontSize = 13.sp, color = MutedGray)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp)) {
                items(filtered.size) { i -> TransactionRow(filtered[i], accentColor) }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
//  PROFILE SCREEN
// ─────────────────────────────────────────────────────────
@Composable
fun ProfileScreen(user: User, vm: AppViewModel, accentColor: Color, startColor: Color, endColor: Color, onBack: () -> Unit, onLogout: () -> Unit) {
    val theme = THEMES[user.type]!!
    var notifs by remember { mutableStateOf(true) }
    var dark   by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Profile header
        Box(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(startColor, endColor)))) {
            Column(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(72.dp).clip(RoundedCornerShape(22.dp)).background(accentColor),
                    contentAlignment = Alignment.Center
                ) { Text(theme.icon, fontSize = 34.sp) }
                Spacer(Modifier.height(12.dp))
                Text(user.name,  color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                Text(user.email, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                PesaChip("${theme.label} · Free Plan", accentColor)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Premium Banner
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFFF5C842), Color(0xFFc8990a))))
                    .padding(16.dp)
            ) {
                Column {
                    Text("⭐ Upgrade to Premium", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color(0xFF333333))
                    Text("AI insights, M-Pesa sync, PDF exports & more", fontSize = 12.sp, color = Color.Black.copy(alpha = 0.5f))
                    Spacer(Modifier.height(6.dp))
                    Text("KES 199/month only", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black.copy(alpha = 0.7f))
                }
            }

            // Account Details
            PesaCard {
                Text("ACCOUNT DETAILS", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MutedGray, letterSpacing = 0.8.sp)
                Spacer(Modifier.height(10.dp))
                listOf(
                    "Full Name"    to user.name,
                    "Email"        to user.email,
                    "Phone"        to user.phone.ifEmpty { "Not set" },
                    "Account Type" to theme.label,
                    "Currency"     to "KES – Kenya Shilling"
                ).forEachIndexed { i, (label, value) ->
                    if (i > 0) HorizontalDivider(color = Color(0xFFF4F7F5))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(label, fontSize = 13.sp, color = MutedGray)
                        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111111))
                    }
                }
            }

            // Settings toggles
            listOf(
                Triple("🔔", "Push Notifications", true),
                Triple("🌙", "Dark Mode",           true),
            ).forEach { (icon, label, isToggle) ->
                PesaCard {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(icon, fontSize = 18.sp)
                        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        if (label == "Push Notifications") PesaToggle(notifs, accentColor) { notifs = it }
                        else PesaToggle(dark, accentColor) { dark = it }
                    }
                }
            }

            listOf(
                Triple("📱", "M-Pesa Integration",  "Premium"),
                Triple("📄", "Export PDF Reports",   "Premium"),
            ).forEach { (icon, label, badge) ->
                PesaCard {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(icon, fontSize = 18.sp)
                        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        PesaChip(badge, Color(0xFFF5C842))
                    }
                }
            }

            listOf("🔒 Security & Privacy", "❓ Help & Support").forEach { label ->
                PesaCard {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        Text("›", color = Color(0xFFCBD5E1), fontSize = 18.sp)
                    }
                }
            }

            PesaButton("🚪 Sign Out", color = DangerRed, onClick = onLogout)
        }
    }
}
