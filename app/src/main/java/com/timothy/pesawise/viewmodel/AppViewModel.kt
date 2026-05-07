package com.timothy.pesawise.viewmodel

import androidx.lifecycle.ViewModel
import com.timothy.pesawise.models.AccountType
import com.timothy.pesawise.models.PieEntry
import com.timothy.pesawise.models.SEED_ACCOUNTS
import com.timothy.pesawise.models.Transaction
import com.timothy.pesawise.models.TransactionType
import com.timothy.pesawise.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

class AppViewModel : ViewModel() {

    // ── All accounts in memory ─────────────────────────────
    private val _accounts = MutableStateFlow(SEED_ACCOUNTS.toMutableList())

    // ── Currently logged-in user ───────────────────────────
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // ── Toast message ──────────────────────────────────────
    private val _toastMsg = MutableStateFlow<String?>(null)
    val toastMsg: StateFlow<String?> = _toastMsg.asStateFlow()

    fun clearToast() { _toastMsg.value = null }

    // ── Auth: Login ───────────────────────────────────────
    fun login(email: String, password: String): Boolean {
        val user = _accounts.value.find {
            it.email.equals(email.trim(), ignoreCase = true) && it.password == password
        }
        return if (user != null) {
            _currentUser.value = user
            true
        } else false
    }

    // ── Auth: Register ────────────────────────────────────
    fun register(
        name: String, email: String, phone: String,
        password: String, type: AccountType
    ): Boolean {
        if (_accounts.value.any { it.email.equals(email, ignoreCase = true) }) return false
        val newUser = User(
            id = System.currentTimeMillis(),
            name = name.trim(),
            email = email.trim(),
            phone = phone.trim(),
            password = password,
            type = type
        )
        _accounts.update { list -> (list + newUser).toMutableList() }
        _currentUser.value = newUser
        _toastMsg.value = "🎉 Welcome, ${newUser.name}! Account created."
        return true
    }

    // ── Auth: Logout ──────────────────────────────────────
    fun logout() { _currentUser.value = null }

    // ── Mutate current user & sync back to accounts list ──
    private fun mutateUser(transform: (User) -> User) {
        _currentUser.update { user ->
            user?.let {
                val updated = transform(it)
                _accounts.update { list ->
                    list.map { acc -> if (acc.id == updated.id) updated else acc }.toMutableList()
                }
                updated
            }
        }
    }

    // ── Add Expense ───────────────────────────────────────
    fun addExpense(amount: Double, category: String, note: String, date: String, icon: String) {
        val tx = Transaction(
            id = System.currentTimeMillis(),
            type = TransactionType.Expense,
            amount = amount,
            category = category,
            note = note,
            date = date,
            icon = icon
        )
        mutateUser { u ->
            val updatedPie = u.pieData.toMutableList().also { pie ->
                val idx = pie.indexOfFirst { it.name == category }
                if (idx >= 0) pie[idx] = pie[idx].copy(value = pie[idx].value + amount)
                else pie.add(PieEntry(category, amount))
            }
            u.copy(
                expenses     = u.expenses + amount,
                balance      = u.balance - amount,
                transactions = listOf(tx) + u.transactions,
                pieData      = updatedPie
            )
        }
        _toastMsg.value = "✅ Expense saved! KES ${"%,.0f".format(amount)}"
    }

    // ── Add Income ────────────────────────────────────────
    fun addIncome(amount: Double, category: String, note: String, icon: String) {
        val tx = Transaction(
            id = System.currentTimeMillis(),
            type = TransactionType.Income,
            amount = amount,
            category = category,
            note = note,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            icon = icon
        )
        mutateUser { u ->
            u.copy(
                income       = u.income + amount,
                balance      = u.balance + amount,
                transactions = listOf(tx) + u.transactions
            )
        }
        _toastMsg.value = "💰 Income saved! KES ${"%,.0f".format(amount)}"
    }

    // ── Helpers ───────────────────────────────────────────
    fun getAccounts(): List<User> = _accounts.value

    fun getSavingsRate(user: User): Int {
        if (user.income == 0.0) return 0
        return ((user.income - user.expenses) / user.income * 100).toInt().coerceIn(0, 100)
    }

    fun getDaysMoneyLasts(user: User): Int {
        val dailySpend = user.expenses / 30.0
        if (dailySpend == 0.0) return 0
        return (user.balance / dailySpend).toInt()
    }

    fun getProfit(user: User): Double = user.income - user.expenses

    fun getProfitMargin(user: User): Int {
        if (user.income == 0.0) return 0
        return ((user.income - user.expenses) / user.income * 100).toInt()
    }

    fun getTotalOwed(user: User): Double = user.customerDebts.sumOf { it.amount }
}
