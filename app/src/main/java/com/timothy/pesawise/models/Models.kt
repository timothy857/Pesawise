package com.timothy.pesawise.models

import com.google.firebase.firestore.IgnoreExtraProperties

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

@IgnoreExtraProperties
data class Transaction(
    val id: Long = System.currentTimeMillis(),
    val type: TransactionType = TransactionType.Expense,
    val amount: Double = 0.0,
    val category: String = "",
    val note: String = "",
    val date: String = "",
    val icon: String = "💰"
)

// ── Goal ──────────────────────────────────────────────────
@IgnoreExtraProperties
data class Goal(
    val id: Int = 0,
    val title: String = "",
    val target: Double = 0.0,
    val saved: Double = 0.0,
    val icon: String = "",
    val colorHex: String = "",
    val deadline: String = ""
)

// ── Bill ──────────────────────────────────────────────────
data class Bill(
    val name: String = "",
    val amount: Double = 0.0,
    val due: String = ""
)

// ── Customer Debt ─────────────────────────────────────────
data class CustomerDebt(
    val name: String = "",
    val amount: Double = 0.0,
    val due: String = ""
)

// ── Business Sale Record ──────────────────────────────────
@IgnoreExtraProperties
data class BusinessSale(
    val id: Long = System.currentTimeMillis(),
    val itemName: String = "",
    val quantity: Int = 0,
    val buyingPrice: Double = 0.0,
    val sellingPrice: Double = 0.0,
    val date: String = ""
)

// ── Pie chart entry ───────────────────────────────────────
data class PieEntry(
    val name: String = "",
    val value: Double = 0.0
)

// ── Monthly chart entry ───────────────────────────────────
data class MonthlyEntry(
    val month: String = "",
    val income: Double = 0.0,
    val expense: Double = 0.0
)

// ── Save Challenge (Student) ──────────────────────────────
data class SaveChallenge(
    val current: Int = 0,
    val target: Int = 0,
    val perDay: Double = 0.0
)

// ── Meal Budget (Student) ─────────────────────────────────
data class MealBudget(
    val daily: Double = 0.0,
    val spent: Double = 0.0,
    val days: Int = 0
)

// ── Student Model ──────────────────────────────────────────
@IgnoreExtraProperties
data class User(
    val userid: String = "",
    val fullname: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val type: AccountType = AccountType.Salaried,

    // financials
    val balance: Double = 0.0,
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val totalSavings: Double = 0.0,

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
    val businessSales: List<BusinessSale> = emptyList(),

    // Student extras
    val semesterBudget: Double = 0.0,
    val semesterSpent: Double = 0.0,
    val saveChallenge: SaveChallenge = SaveChallenge(0, 30, 50.0),
    val mealBudget: MealBudget = MealBudget(300.0, 0.0, 1)
)

@IgnoreExtraProperties
data class Student(
    val userid: String = "",
    val fullname: String = "",
    val email: String = "",
    val balance: Double = 0.0,
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val totalSavings: Double = 0.0,
    val semesterBudget: Double = 0.0,
    val semesterSpent: Double = 0.0,
    val mealBudget: MealBudget = MealBudget(300.0, 0.0, 1),
    val saveChallenge: SaveChallenge = SaveChallenge(0, 30, 50.0),
    val transactions: List<Transaction> = emptyList()
)

// ── Expense categories ────────────────────────────────────
data class Category(
    val name: String = "",
    val icon: String = ""
)

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
