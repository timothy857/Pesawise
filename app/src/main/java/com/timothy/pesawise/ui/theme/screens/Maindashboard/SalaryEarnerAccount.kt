package com.timothy.pesawise.ui.theme.screens.Maindashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timothy.pesawise.models.*
import com.timothy.pesawise.viewmodel.AppViewModel
import com.timothy.pesawise.ui.theme.*
import com.timothy.pesawise.navigation.*

// Color Palette
val DashboardPrimary = SalariedGreen
val DashboardSecondary = RoyalBlue
val DashboardIncome = SuccessGreen
val DashboardExpense = ErrorRed
val DashboardBackground = SoftGray

val dummyUser = User(
    fullname = "Alex Wanjiku",
    email = "alex@example.com",
    password = "password",
    type = AccountType.Salaried,
    balance = 28450.0,
    income = 45000.0,
    expenses = 16550.0,
    loanBalance = 15000.0,
    loanMonthly = 3000.0,
    bills = listOf(
        Bill("KPLC", 800.0, "May 5"),
        Bill("Netflix", 1100.0, "May 8"),
        Bill("Wifi", 2500.0, "May 10")
    ),
    transactions = listOf(
        Transaction(
            type = TransactionType.Income,
            amount = 45000.0,
            category = "Salary",
            note = "April salary - KCB Bank",
            icon = "💼"
        ),
        Transaction(
            type = TransactionType.Expense,
            amount = 12000.0,
            category = "Rent",
            note = "House rent - April",
            icon = "🏠"
        ),
        Transaction(
            type = TransactionType.Expense,
            amount = 2800.0,
            category = "Food",
            note = "Supermarket groceries",
            icon = "🛒"
        )
    )
)

@Composable
fun SalaryEarnerAccount(
    navController: NavHostController,
    viewModel: AppViewModel = viewModel()
) {
    val userState by viewModel.currentUser.collectAsStateWithLifecycle()
    val user = userState ?: dummyUser

    Scaffold(
        bottomBar = {
            PesaBottomNav(
                nav = navController,
                currentRoute = ROUTE_DASHBOARD,
                accentColor = DashboardIncome,
                primaryColor = DashboardPrimary
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ROUTE_ADD_EXPENSE) },
                containerColor = DashboardPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.offset(y = 50.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = DashboardBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { HeaderSection(user) }
            item { SurvivalMeterSection(viewModel.getDaysMoneyLasts(user)) }
            item { QuickActionsSection(navController) }
            item { BillsDueSoonSection(user.bills) }
            item { LoanTrackerSection(user.loanBalance, user.loanMonthly) }
            item { AIInsightSection() }
            item { RecentTransactionsSection(navController, user.transactions) }
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun HeaderSection(user: User) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DashboardPrimary, DashboardSecondary)
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "GOOD MORNING 💼",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user.fullname,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Surface(
                    modifier = Modifier.size(45.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TOTAL BALANCE",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "KES ${user.balance}",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem(label = "INCOME", value = "KES ${user.income}", color = Color(0xFF81C784))
                        StatItem(label = "EXPENSES", value = "KES ${user.expenses}", color = Color(0xFFE57373))
                        StatItem(label = "SAVINGS", value = "KES ${user.totalSavings}", color = Color(0xFFFFD54F))
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SurvivalMeterSection(days: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = DashboardPrimary)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$days",
                color = Color(0xFFFFD54F),
                fontSize = 48.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Salary Survival Meter 💸",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Days your money can last at current spending",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun QuickActionsSection(navController: NavHostController) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.ShoppingCart,
                label = "Add Expense",
                iconColor = Color(0xFFF06292),
                onClick = { navController.navigate(ROUTE_ADD_EXPENSE) }
            )
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Add,
                label = "Add Income",
                iconColor = Color(0xFF66BB6A),
                onClick = { navController.navigate(ROUTE_ADD_INCOME) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.List,
                label = "Reports",
                iconColor = Color(0xFF4FC3F7),
                onClick = { navController.navigate(ROUTE_REPORTS) }
            )
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Star,
                label = "Goals",
                iconColor = Color(0xFFD32F2F),
                onClick = { navController.navigate(ROUTE_GOALS) }
            )
        }
    }
}

@Composable
fun QuickActionCard(modifier: Modifier, icon: ImageVector, label: String, iconColor: Color, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier.height(80.dp).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = iconColor.copy(alpha = 0.1f)
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        }
    }
}

@Composable
fun BillsDueSoonSection(bills: List<Bill>) {
    Column(modifier = Modifier.padding(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.List, contentDescription = null, tint = Color(0xFF795548), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Bills Due Soon", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        bills.forEach { bill ->
            BillItem(bill.name, "Due: ${bill.due}", "KES ${bill.amount}")
        }
    }
}

@Composable
fun BillItem(name: String, due: String, amount: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color(0xFFFFEBEE)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFFFFD54F), modifier = Modifier.padding(8.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = due, color = Color.Gray, fontSize = 12.sp)
                }
            }
            Text(text = amount, color = DashboardExpense, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LoanTrackerSection(balance: Double, monthly: Double) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF00796B), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Loan Tracker", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Balance", color = Color.Gray)
                Text(text = "KES $balance", color = DashboardExpense, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Monthly", color = Color.Gray)
                Text(text = "KES $monthly", color = DashboardIncome, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { if (balance + monthly > 0) (monthly / (balance + monthly)).toFloat() else 0f },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = DashboardPrimary,
                trackColor = DashboardPrimary.copy(alpha = 0.1f),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            Spacer(modifier = Modifier.height(8.dp))
            val monthsRemaining = if (monthly > 0) (balance / monthly).toInt() else 0
            Text(text = "~$monthsRemaining months remaining", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun AIInsightSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Face, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "AI INSIGHT", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "You saved KES 28,450 this month. Cut food by 15% and hit your laptop goal by October! 💡",
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun RecentTransactionsSection(navController: NavHostController, transactions: List<Transaction>) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Recent Transactions", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "See All →",
                color = DashboardIncome,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate(ROUTE_HISTORY) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        transactions.forEach { transaction ->
            TransactionItem(
                title = transaction.category,
                desc = transaction.note,
                amount = if (transaction.type == TransactionType.Income) "+KES ${transaction.amount}" else "-KES ${transaction.amount}",
                color = if (transaction.type == TransactionType.Income) DashboardIncome else DashboardExpense,
                iconEmoji = transaction.icon
            )
        }
    }
}

@Composable
fun TransactionItem(title: String, desc: String, amount: String, color: Color, iconEmoji: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = DashboardPrimary.copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = iconEmoji, fontSize = 24.sp)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = desc, color = Color.Gray, fontSize = 12.sp)
                }
            }
            Text(text = amount, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SalaryEarnerAccountPreview() {
    SalaryEarnerAccount(rememberNavController())
}
