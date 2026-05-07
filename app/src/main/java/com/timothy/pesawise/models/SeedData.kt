package com.timothy.pesawise.models

val SEED_ACCOUNTS = mutableListOf(
    User(
        id = 1L, name = "Alex Wanjiku", email = "alex@salary.com",
        phone = "0712 345 678", password = "pass123", type = AccountType.Salaried,
        balance = 28450.0, income = 45000.0, expenses = 16550.0,
        loanBalance = 15000.0, loanMonthly = 3000.0, payday = 25,
        bills = listOf(
            Bill("KPLC",    800.0,  "May 5"),
            Bill("Netflix", 1100.0, "May 8"),
            Bill("Wifi",    2500.0, "May 10")
        ),
        transactions = listOf(
            Transaction(1L, TransactionType.Income,  45000.0, "Salary",    "April salary – KCB Bank",  "2025-04-25", "💼"),
            Transaction(2L, TransactionType.Expense, 12000.0, "Rent",      "House rent – April",        "2025-04-26", "🏠"),
            Transaction(3L, TransactionType.Expense,  2800.0, "Food",      "Supermarket groceries",     "2025-04-27", "🍽️"),
            Transaction(4L, TransactionType.Expense,   500.0, "Transport", "Matatu fare weekly",         "2025-04-27", "🚌"),
            Transaction(5L, TransactionType.Expense,  1250.0, "Airtime",   "Safaricom data",            "2025-04-24", "📱")
        ),
        goals = listOf(
            Goal(1, "Emergency Fund", 50000.0, 38000.0, "🛡️", "#00C896", "Jun 2025"),
            Goal(2, "Buy Laptop",     60000.0, 22000.0, "💻", "#4ECDC4", "Dec 2025")
        ),
        pieData = listOf(
            PieEntry("Rent",      12000.0),
            PieEntry("Food",       2800.0),
            PieEntry("Transport",   500.0),
            PieEntry("Airtime",    1250.0)
        ),
        monthly = listOf(
            MonthlyEntry("Nov", 42000.0, 19200.0),
            MonthlyEntry("Dec", 45000.0, 21000.0),
            MonthlyEntry("Jan", 45000.0, 18400.0),
            MonthlyEntry("Feb", 45000.0, 17200.0),
            MonthlyEntry("Mar", 47000.0, 20100.0),
            MonthlyEntry("Apr", 45000.0, 16550.0)
        )
    ),
    User(
        id = 2L, name = "John Kamau", email = "john@business.com",
        phone = "0722 987 654", password = "pass123", type = AccountType.Business,
        balance = 62300.0, income = 98000.0, expenses = 35700.0,
        todaySales = 28000.0, todayExpenses = 8000.0, stockValue = 145000.0,
        customerDebts = listOf(
            CustomerDebt("Mercy Achieng",  12000.0, "May 3"),
            CustomerDebt("Brian Otieno",    5500.0, "May 10"),
            CustomerDebt("Fatuma Hassan",   8800.0, "Apr 30")
        ),
        transactions = listOf(
            Transaction(1L, TransactionType.Income,  28000.0, "Sales",     "Week 3 shop sales",       "2025-04-27", "🛒"),
            Transaction(2L, TransactionType.Income,  15000.0, "Sales",     "Wholesale order – Mercy", "2025-04-25", "🛒"),
            Transaction(3L, TransactionType.Expense, 22000.0, "Stock",     "Restocked electronics",   "2025-04-26", "📦"),
            Transaction(4L, TransactionType.Expense,  5000.0, "Rent",      "Shop rent – Ngara",       "2025-04-20", "🏪"),
            Transaction(5L, TransactionType.Expense,  3200.0, "Transport", "Delivery courier",        "2025-04-24", "🚌")
        ),
        goals = listOf(
            Goal(1, "Open Branch",  200000.0,  62000.0, "🏢", "#F59E0B", "Jan 2026"),
            Goal(2, "Delivery Van", 800000.0, 120000.0, "🚐", "#34D399", "Dec 2026")
        ),
        pieData = listOf(
            PieEntry("Stock",      22000.0),
            PieEntry("Rent",        5000.0),
            PieEntry("Transport",   3200.0),
            PieEntry("Other",       5500.0)
        ),
        monthly = listOf(
            MonthlyEntry("Nov",  80000.0, 38000.0),
            MonthlyEntry("Dec", 110000.0, 45000.0),
            MonthlyEntry("Jan",  85000.0, 32000.0),
            MonthlyEntry("Feb",  90000.0, 36000.0),
            MonthlyEntry("Mar",  95000.0, 40000.0),
            MonthlyEntry("Apr",  98000.0, 35700.0)
        )
    ),
    User(
        id = 3L, name = "Jane Njeri", email = "jane@student.com",
        phone = "0701 234 567", password = "pass123", type = AccountType.Student,
        balance = 4250.0, income = 8000.0, expenses = 3750.0,
        semesterBudget = 48000.0, semesterSpent = 31200.0,
        saveChallenge = SaveChallenge(18, 30, 50.0),
        mealBudget    = MealBudget(300.0, 1200.0, 4),
        transactions = listOf(
            Transaction(1L, TransactionType.Income,  5000.0, "Allowance", "Parents – April",      "2025-04-01", "🏠"),
            Transaction(2L, TransactionType.Income,  3000.0, "HELB",      "HELB upkeep – April",  "2025-04-05", "🎓"),
            Transaction(3L, TransactionType.Expense, 1200.0, "Food",      "Canteen meals",         "2025-04-27", "🍽️"),
            Transaction(4L, TransactionType.Expense,  300.0, "Transport", "Matatu to campus",      "2025-04-26", "🚌"),
            Transaction(5L, TransactionType.Expense,  800.0, "School",    "Photocopy & printing",  "2025-04-24", "📚"),
            Transaction(6L, TransactionType.Expense,  600.0, "Airtime",   "Airtel data bundles",   "2025-04-23", "📱")
        ),
        goals = listOf(
            Goal(1, "New Phone", 25000.0, 7500.0, "📱", "#38BDF8", "Aug 2025"),
            Goal(2, "DSTV Sub",   3000.0, 1800.0, "📺", "#FCD34D", "May 2025")
        ),
        pieData = listOf(
            PieEntry("Food",      1200.0),
            PieEntry("Transport",  300.0),
            PieEntry("School",     800.0),
            PieEntry("Airtime",    600.0),
            PieEntry("Other",      850.0)
        ),
        monthly = listOf(
            MonthlyEntry("Nov", 7000.0, 4200.0),
            MonthlyEntry("Dec", 5000.0, 3000.0),
            MonthlyEntry("Jan", 8000.0, 5100.0),
            MonthlyEntry("Feb", 8000.0, 4200.0),
            MonthlyEntry("Mar", 8000.0, 3800.0),
            MonthlyEntry("Apr", 8000.0, 3750.0)
        )
    )
)
