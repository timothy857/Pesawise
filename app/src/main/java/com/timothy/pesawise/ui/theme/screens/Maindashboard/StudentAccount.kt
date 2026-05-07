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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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

// Color Palette for Student
val StudentPrimary = StudentBlue
val StudentSecondary = RoyalBlue
val StudentAccent = SkyBlue
val StudentHighlight = GoldAccent
val StudentBackground = SoftGray

val dummyStudentUser = User(
    name = "Faith Mutua",
    email = "faith@example.com",
    password = "password",
    type = AccountType.Student,
    balance = 4200.0,
    income = 15000.0,
    expenses = 10800.0,
    semesterBudget = 50000.0,
    semesterSpent = 32000.0,
    saveChallenge = SaveChallenge(12, 30, 50.0),
    mealBudget = MealBudget(300.0, 180.0, 1),
    transactions = listOf(
        Transaction(
            type = TransactionType.Income,
            amount = 5000.0,
            category = "Allowance",
            note = "Parents - April",
            icon = "🏠"
        ),
        Transaction(
            type = TransactionType.Income,
            amount = 3000.0,
            category = "HELB",
            note = "HELB upkeep - April",
            icon = "🎓"
        ),
        Transaction(
            type = TransactionType.Expense,
            amount = 1200.0,
            category = "Food",
            note = "Canteen meals",
            icon = "🍽️"
        )
    )
)

@Composable
fun StudentAccount(
    navController: NavHostController,
    viewModel: AppViewModel = viewModel()
) {
    val userState by viewModel.currentUser.collectAsStateWithLifecycle()
    val user = userState ?: dummyStudentUser

    Scaffold(
        bottomBar = {
            PesaBottomNav(
                nav = navController,
                currentRoute = ROUTE_DASHBOARD,
                accentColor = StudentAccent,
                primaryColor = StudentPrimary
            )
        },
        containerColor = StudentBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { StudentHeaderSection(user) }
            item { SemesterBudgetSection(user.semesterBudget, user.semesterSpent) }
            item { StudentQuickActionsSection(navController) }
            item { MealBudgetSection(user.mealBudget) }
            item { SaveChallengeSection(user.saveChallenge) }
            item { StudentInsightSection() }
            item { StudentRecentActivitySection(user.transactions) }
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun StudentHeaderSection(user: User) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(StudentPrimary, StudentSecondary)
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
                        text = "CAMPUS MODE \uD83C\uDF93",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user.name,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Surface(
                    modifier = Modifier.size(45.dp),
                    shape = CircleShape,
                    color = StudentHighlight
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = StudentPrimary,
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
                        text = "AVAILABLE BALANCE",
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
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StudentStatItem(label = "SPENT TODAY", value = "KES 180", color = StudentAccent)
                        StudentStatItem(label = "SAVED", value = "KES 1,200", color = StudentHighlight)
                    }
                }
            }
        }
    }
}

@Composable
fun StudentStatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SemesterBudgetSection(total: Double, spent: Double) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            LinearProgressIndicator(
                progress = { if (total > 0) (spent / total).toFloat() else 0f },
                modifier = Modifier.fillMaxWidth().height(24.dp),
                color = StudentAccent,
                trackColor = StudentAccent.copy(alpha = 0.1f),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "KES ${spent.toInt()} used",
                    color = StudentAccent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "KES ${(total - spent).toInt()} left",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun StudentQuickActionsSection(navController: NavHostController) {
    Row(
        modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StudentActionCard(
            Modifier.weight(1f), 
            "Add Expense", 
            "💸", 
            StudentAccent,
            onClick = { navController.navigate(ROUTE_ADD_EXPENSE) }
        )
        StudentActionCard(
            Modifier.weight(1f), 
            "Savings Challenge", 
            "🎯", 
            StudentHighlight,
            onClick = { navController.navigate(ROUTE_GOALS) }
        )
    }
}

@Composable
fun StudentActionCard(modifier: Modifier, label: String, emoji: String, color: Color, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier.height(100.dp).clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun MealBudgetSection(mealBudget: MealBudget) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = StudentPrimary)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🍱", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Daily Meal Budget", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Column {
                    Text(text = "Remaining", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text(text = "KES ${mealBudget.daily - mealBudget.spent}", color = StudentHighlight, fontSize = 28.sp, fontWeight = FontWeight.Black)
                }
                Text(text = "Budget: KES ${mealBudget.daily}", color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun SaveChallengeSection(challenge: SaveChallenge) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudentHighlight.copy(alpha = 0.5f))
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { if (challenge.target > 0) challenge.current.toFloat() / challenge.target else 0f },
                    modifier = Modifier.size(60.dp),
                    color = StudentHighlight,
                    trackColor = StudentHighlight.copy(alpha = 0.1f),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                Text(text = "${challenge.current}/${challenge.target}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(text = "30-Day Savings Challenge", fontWeight = FontWeight.Bold)
                Text(text = "Save KES ${challenge.perDay} daily to hit your goal!", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun StudentInsightSection() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = StudentPrimary)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🤖", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "STUDENT TIP",
                    color = StudentAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = buildAnnotatedString {
                    append("Day ")
                    withStyle(style = SpanStyle(color = StudentHighlight, fontWeight = FontWeight.Bold)) {
                        append("18")
                    }
                    append(" streak! You've stashed ")
                    withStyle(style = SpanStyle(color = StudentHighlight, fontWeight = FontWeight.Bold)) {
                        append("KES 900")
                    }
                    append(".\nCook at home twice a week — save 30% on meals. 🍳")
                },
                color = Color.White,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun StudentRecentActivitySection(transactions: List<Transaction>) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Spending",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "See All →",
                color = StudentAccent,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        transactions.forEach { transaction ->
            StudentActivityItem(
                title = transaction.category,
                desc = transaction.note,
                amount = if (transaction.type == TransactionType.Income) "+KES ${transaction.amount.toInt()}" else "-KES ${transaction.amount.toInt()}",
                color = if (transaction.type == TransactionType.Income) StudentAccent else Color(0xFFFF5252),
                emoji = transaction.icon
            )
        }
    }
}

@Composable
fun StudentActivityItem(title: String, desc: String, amount: String, color: Color, emoji: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = color.copy(alpha = 0.08f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = emoji, fontSize = 28.sp)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = desc,
                        color = Color.Gray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Text(
                text = amount,
                color = color,
                fontWeight = FontWeight.Black,
                fontSize = 17.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentAccountPreview() {
    StudentAccount(rememberNavController())
}
