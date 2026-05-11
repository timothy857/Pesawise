package com.timothy.pesawise.ui.theme.screens.Maindashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timothy.pesawise.models.*
import com.timothy.pesawise.viewmodel.AppViewModel
import com.timothy.pesawise.ui.components.*
import com.timothy.pesawise.ui.theme.*
import com.timothy.pesawise.navigation.*

// Color Palette for Business
val BizPrimary = BusinessPurple
val BizSecondary = RoyalBlue
val BizAccent = GoldAccent
val BizExpense = ErrorRed
val BizIncome = SuccessGreen
val BizBackground = SoftGray

val dummyBusinessUser = User(
    fullname = "John Kamau",
    email = "john@example.com",
    password = "password",
    type = AccountType.Business,
    balance = 62300.0,
    income = 98000.0,
    expenses = 35700.0,
    todaySales = 28000.0,
    todayExpenses = 8000.0,
    stockValue = 145000.0,
    customerDebts = listOf(
        CustomerDebt("Mercy Achieng", 12000.0, "May 3"),
        CustomerDebt("Brian Otieno", 5500.0, "May 10"),
        CustomerDebt("Fatuma Hassan", 8800.0, "Apr 30")
    ),
    transactions = listOf(
        Transaction(
            type = TransactionType.Income,
            amount = 28000.0,
            category = "Sales",
            note = "Week 3 shop sales",
            icon = "🛒"
        ),
        Transaction(
            type = TransactionType.Income,
            amount = 15000.0,
            category = "Sales",
            note = "Wholesale order - Mercy",
            icon = "🛒"
        ),
        Transaction(
            type = TransactionType.Expense,
            amount = 22000.0,
            category = "Stock",
            note = "Restocked electronics",
            icon = "📦"
        )
    )
)

@Composable
fun BusinessAccount(
    navController: NavHostController,
    viewModel: AppViewModel = viewModel()
) {
    val userState by viewModel.currentUser.collectAsStateWithLifecycle()
    val user = userState ?: dummyBusinessUser
    
    Scaffold(
        bottomBar = {
            PesaBottomNav(
                nav = navController,
                currentRoute = ROUTE_DASHBOARD,
                accentColor = BizAccent,
                primaryColor = BizPrimary
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ROUTE_ADD_EXPENSE) },
                containerColor = BizPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.offset(y = 50.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = BizBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { BusinessHeaderSection(user) }
            item { TodaySummarySection(user.todaySales, user.todayExpenses) }
            item { BusinessQuickActionsSection(navController) }
            item { ProfitCalculatorSection() }
            item { 
                CustomersOweSection(
                    debts = user.customerDebts,
                    onAddDebt = { name, amt, due -> viewModel.addCustomerDebt(name, amt, due) }
                ) 
            }
            item { StockValueSection(user.stockValue) }
            item { BusinessInsightSection() }
            item { RecentActivitySection(navController, user.transactions) }
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun BusinessHeaderSection(user: User) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BizPrimary, BizSecondary)
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
                        text = "BUSINESS HQ 🏪",
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
                    shape = RoundedCornerShape(12.dp),
                    color = BizAccent
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = BizPrimary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CASH BALANCE",
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
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        BusinessStatItem(label = "REVENUE", value = "KES ${user.income}", color = BizAccent)
                        BusinessStatItem(label = "EXPENSES", value = "KES ${user.expenses}", color = BizExpense)
                        BusinessStatItem(label = "SAVINGS", value = "KES ${user.totalSavings}", color = BizIncome)
                    }
                }
            }
        }
    }
}

@Composable
fun BusinessStatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun TodaySummarySection(sales: Double, costs: Double) {
    Column(modifier = Modifier.padding(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "📅", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Today's Summary", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummarySmallCard(Modifier.weight(1f), "Sales", "KES $sales", BizAccent)
            SummarySmallCard(Modifier.weight(1f), "Costs", "KES $costs", BizExpense)
            SummarySmallCard(Modifier.weight(1f), "Profit", "KES ${sales - costs}", BizIncome)
        }
    }
}

@Composable
fun SummarySmallCard(modifier: Modifier, label: String, value: String, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = label, color = Color.Gray, fontSize = 11.sp)
        }
    }
}

@Composable
fun ProfitCalculatorSection() {
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var buyingPrice by remember { mutableStateOf("") }
    var sellingPrice by remember { mutableStateOf("") }

    val q = quantity.toDoubleOrNull() ?: 0.0
    val bp = buyingPrice.toDoubleOrNull() ?: 0.0
    val sp = sellingPrice.toDoubleOrNull() ?: 0.0

    val totalBuying = q * bp
    val totalSelling = q * sp
    val profit = totalSelling - totalBuying

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📈", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Profit Calculator", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            PesaInput(value = itemName, onValueChange = { itemName = it }, label = "Item Name", placeholder = "e.g. Bread")
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    PesaInput(value = quantity, onValueChange = { quantity = it }, label = "Qty", placeholder = "0", keyboardType = KeyboardType.Number)
                }
                Box(modifier = Modifier.weight(1f)) {
                    PesaInput(value = buyingPrice, onValueChange = { buyingPrice = it }, label = "BP", placeholder = "0", keyboardType = KeyboardType.Number)
                }
                Box(modifier = Modifier.weight(1f)) {
                    PesaInput(value = sellingPrice, onValueChange = { sellingPrice = it }, label = "SP", placeholder = "0", keyboardType = KeyboardType.Number)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = SoftGray)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Column {
                    Text("Total Profit/Loss", color = Color.Gray, fontSize = 12.sp)
                    Text(
                        text = "KES ${"%,.0f".format(profit)}",
                        color = if (profit >= 0) BizIncome else BizExpense,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp
                    )
                }
                if (profit != 0.0) {
                    val margin = if (totalSelling > 0) (profit / totalSelling * 100).toInt() else 0
                    Surface(
                        color = (if (profit >= 0) BizIncome else BizExpense).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (profit > 0) "+$margin% Margin" else "$margin% Margin",
                            color = if (profit >= 0) BizIncome else BizExpense,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BusinessQuickActionsSection(navController: NavHostController) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BusinessActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.ShoppingCart,
                label = "Inventory/Sales",
                iconColor = Color(0xFFFFE0B2),
                onClick = { navController.navigate(ROUTE_SALES) }
            )
            BusinessActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Add,
                label = "Add Expense",
                iconColor = Color(0xFFF8BBD0),
                onClick = { navController.navigate(ROUTE_ADD_EXPENSE) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BusinessActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Star, // Changed to Star or something for Income
                label = "Add Income",
                iconColor = Color(0xFFC8E6C9),
                onClick = { navController.navigate(ROUTE_ADD_INCOME) }
            )
            BusinessActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.AutoMirrored.Filled.List,
                label = "P&L Report",
                iconColor = Color(0xFFE1F5FE),
                onClick = { navController.navigate(ROUTE_REPORTS) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BusinessActionCard(
                modifier = Modifier.weight(0.5f),
                icon = Icons.Default.Star,
                label = "Biz Goals",
                iconColor = Color(0xFFFFEBEE),
                onClick = { navController.navigate(ROUTE_GOALS) }
            )
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
fun BusinessActionCard(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(12.dp),
                color = iconColor
            ) {
                Icon(icon, contentDescription = null, tint = BizPrimary.copy(alpha = 0.7f), modifier = Modifier.padding(10.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
fun CustomersOweSection(debts: List<CustomerDebt>, onAddDebt: (String, Double, String) -> Unit) {
    var showForm by remember { mutableStateOf(false) }
    var customerName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "💳", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Customers Owe You", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = { showForm = !showForm }) {
                Text(if (showForm) "✕" else "＋", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BizAccent)
            }
        }

        if (showForm) {
            PesaCard(modifier = Modifier.padding(vertical = 12.dp)) {
                Text("RECORD NEW DEBT", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BizAccent)
                Spacer(Modifier.height(16.dp))
                PesaInput(customerName, { customerName = it }, "Customer Name", "e.g. Mary Atieno")
                Spacer(Modifier.height(12.dp))
                PesaInput(amount, { amount = it }, "Amount Owed", "0", keyboardType = KeyboardType.Number)
                Spacer(Modifier.height(12.dp))
                PesaInput(dueDate, { dueDate = it }, "Due Date", "e.g. May 15")
                Spacer(Modifier.height(20.dp))
                PesaButton("Save Debt", color = BizAccent, enabled = customerName.isNotEmpty() && amount.isNotEmpty()) {
                    onAddDebt(customerName, amount.toDoubleOrNull() ?: 0.0, dueDate)
                    customerName = ""; amount = ""; dueDate = ""; showForm = false
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Total: KES ${debts.sumOf { it.amount }}",
            color = BizAccent,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        debts.forEach { debt ->
            CustomerOweItem(debt.name, "Due: ${debt.due}", "KES ${debt.amount}")
        }
    }
}

@Composable
fun CustomerOweItem(name: String, due: String, amount: String) {
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
                    color = Color(0xFFE8EAF6)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF3F51B5), modifier = Modifier.padding(8.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = due, color = Color.Gray, fontSize = 12.sp)
                }
            }
            Text(text = amount, color = BizAccent, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StockValueSection(value: Double) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BizAccent.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "📦", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "Stock Value", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "Current inventory worth", color = Color.Gray, fontSize = 12.sp)
                }
            }
            Text(text = "KES $value", color = BizAccent, fontWeight = FontWeight.Black, fontSize = 18.sp)
        }
    }
}

@Composable
fun BusinessInsightSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BizPrimary)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Face, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "BUSINESS INSIGHT", color = BizAccent, fontSize = 12.sp, fontWeight = FontWeight.Black)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Profit margin: 64%. Customers owe KES 26,300 – follow up this week! 📞",
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun RecentActivitySection(navController: NavHostController, transactions: List<Transaction>) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Recent Activity", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "See All →",
                color = BizAccent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate(ROUTE_HISTORY) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        transactions.forEach { activity ->
            ActivityItem(
                title = activity.category,
                desc = activity.note,
                amount = if (activity.type == TransactionType.Income) "+KES ${activity.amount}" else "-KES ${activity.amount}",
                color = if (activity.type == TransactionType.Income) BizAccent else BizExpense,
                iconEmoji = activity.icon
            )
        }
    }
}

@Composable
fun ActivityItem(title: String, desc: String, amount: String, color: Color, iconEmoji: String) {
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
                    color = color.copy(alpha = 0.1f)
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
fun BusinessAccountPreview() {
    BusinessAccount(rememberNavController())
}
