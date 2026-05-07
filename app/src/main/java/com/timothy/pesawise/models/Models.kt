package com.timothy.pesawise.models

// ── Account types ─────────────────────────────────────────
enum class AccountType { Salaried, Business, Student }

// ── Theme config per account type ─────────────────────────
data class AppTheme(
    val primaryHex: String,
    val accentHex: String,
    val lightHex: String,
    val goldHex: String,
    val gradientStart: String,
    val gradientEnd: String,
    val icon: String,
    val label: String
)

val THEMES = mapOf(
    AccountType.Salaried to AppTheme(
        primaryHex   = "#0D3B2E",
        accentHex    = "#00C896",
        lightHex     = "#E6F7F2",
        goldHex      = "#F5C842",
        gradientStart= "#0D3B2E",
        gradientEnd  = "#1a5e47",
        icon         = "💼",
        label        = "Salary Earner"
    ),
    AccountType.Business to AppTheme(
        primaryHex   = "#1C1235",
        accentHex    = "#F59E0B",
        lightHex     = "#FEF3C7",
        goldHex      = "#34D399",
        gradientStart= "#1C1235",
        gradientEnd  = "#2d1b69",
        icon         = "🏪",
        label        = "Business Owner"
    ),
    AccountType.Student to AppTheme(
        primaryHex   = "#0C3547",
        accentHex    = "#38BDF8",
        lightHex     = "#E0F2FE",
        goldHex      = "#FCD34D",
        gradientStart= "#0C3547",
        gradientEnd  = "#0e5272",
        icon         = "🎓",
        label        = "Student"
    )
)

// ── Transaction ────────────────────────────────────────────
enum class TransactionType { Income, Expense }

data class Transaction(
    val id: Long = System.currentTimeMillis(),
    val type: TransactionType,
    val amount: Double,
    val category: String,
    val note: String = "",
    val date: String = "",
    val icon: String = "💰"
)

// ── Goal ──────────────────────────────────────────────────
data class Goal(
    val id: Int,
    val title: String,
    val target: Double,
    val saved: Double,
    val icon: String,
    val colorHex: String,
    val deadline: String
)

// ── Bill ──────────────────────────────────────────────────
data class Bill(val name: String, val amount: Double, val due: String)

// ── Customer Debt ─────────────────────────────────────────
data class CustomerDebt(val name: String, val amount: Double, val due: String)

// ── Pie chart entry ───────────────────────────────────────
data class PieEntry(val name: String, val value: Double)

// ── Monthly chart entry ───────────────────────────────────
data class MonthlyEntry(val month: String, val income: Double, val expense: Double)

// ── Save Challenge (Student) ──────────────────────────────
data class SaveChallenge(val current: Int, val target: Int, val perDay: Double)

// ── Meal Budget (Student) ─────────────────────────────────
data class MealBudget(val daily: Double, val spent: Double, val days: Int)

// ── Main User ─────────────────────────────────────────────
data class User(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val email: String,
    val phone: String = "",
    val password: String,
    val type: AccountType,

    // financials
    val balance: Double = 0.0,
    val income: Double = 0.0,
    val expenses: Double = 0.0,

    // transactions & goals
    val transactions: List<Transaction> = emptyList(),
    val goals: List<Goal> = emptyList(),
    val pieData: List<PieEntry> = emptyList(),
    val monthly: List<MonthlyEntry> = emptyList(),

    // Salaried extras
    val loanBalance: Double = 0.0,
    val loanMonthly: Double = 0.0,
    val payday: Int = 25,
    val bills: List<Bill> = emptyList(),

    // Business extras
    val todaySales: Double = 0.0,
    val todayExpenses: Double = 0.0,
    val stockValue: Double = 0.0,
    val customerDebts: List<CustomerDebt> = emptyList(),

    // Student extras
    val semesterBudget: Double = 0.0,
    val semesterSpent: Double = 0.0,
    val saveChallenge: SaveChallenge = SaveChallenge(0, 30, 50.0),
    val mealBudget: MealBudget = MealBudget(300.0, 0.0, 1)
)

// ── Expense categories ────────────────────────────────────
data class Category(val name: String, val icon: String)

val EXPENSE_CATEGORIES = listOf(
    Category("Food",          "🍽️"),
    Category("Rent",          "🏠"),
    Category("Transport",     "🚌"),
    Category("Airtime",       "📱"),
    Category("Entertainment", "🎬"),
    Category("School",        "📚"),
    Category("Stock",         "📦"),
    Category("Health",        "💊"),
    Category("Utilities",     "💡"),
    Category("Other",         "🗂️")
)

val PIE_COLORS = listOf(
    0xFFFF6B6B, 0xFF4ECDC4, 0xFFFFE66D, 0xFFA8E6CF,
    0xFFC5A3FF, 0xFFFDB99B, 0xFF38BDF8, 0xFFF59E0B, 0xFF84CC16
)
