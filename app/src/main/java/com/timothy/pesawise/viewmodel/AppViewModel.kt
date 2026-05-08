package com.timothy.pesawise.viewmodel

import androidx.lifecycle.ViewModel
import com.timothy.pesawise.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

class AppViewModel : ViewModel() {

    private val _accounts = MutableStateFlow<List<User>>(emptyList())
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _toastMsg = MutableStateFlow<String?>(null)
    val toastMsg: StateFlow<String?> = _toastMsg.asStateFlow()

    fun clearToast() { _toastMsg.value = null }

    fun login(email: String, password: String): Boolean {
        val user = _accounts.value.find {
            it.email.equals(email.trim(), ignoreCase = true) && it.password == password
        }
        return if (user != null) { _currentUser.value = user; true } else false
    }

    fun register(name: String, email: String, phone: String, password: String, type: AccountType): Boolean {
        if (_accounts.value.any { it.email.equals(email, ignoreCase = true) }) return false
        val newUser = User(
            id = System.currentTimeMillis(), name = name.trim(),
            email = email.trim(), phone = phone.trim(), password = password, type = type
        )
        _accounts.update { (it + newUser).toMutableList() }
        _currentUser.value = newUser
        _toastMsg.value = "🎉 Welcome, ${newUser.name}! Account created."
        return true
    }

    fun logout() { _currentUser.value = null }

    fun setCurrentUser(user: User) {
        _currentUser.value = user
    }

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

    fun addExpense(amount: Double, category: String, note: String, date: String, icon: String) {
        val tx = Transaction(
            id = System.currentTimeMillis(), type = TransactionType.Expense,
            amount = amount, category = category, note = note, date = date, icon = icon
        )
        mutateUser { u ->
            val updatedPie = u.pieData.toMutableList().apply {
                val idx = indexOfFirst { it.name == category }
                if (idx >= 0) set(idx, get(idx).copy(value = get(idx).value + amount))
                else add(PieEntry(category, amount))
            }
            u.copy(
                expenses = u.expenses + amount,
                balance  = u.balance  - amount,
                transactions = listOf(tx) + u.transactions,
                pieData = updatedPie
            )
        }
        _toastMsg.value = "✅ Expense saved! KES ${"%,.0f".format(amount)}"
    }

    fun addIncome(amount: Double, category: String, note: String, icon: String) {
        val tx = Transaction(
            id = System.currentTimeMillis(), type = TransactionType.Income,
            amount = amount, category = category, note = note,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            icon = icon
        )
        mutateUser { u ->
            u.copy(
                income = u.income + amount,
                balance = u.balance + amount,
                transactions = listOf(tx) + u.transactions
            )
        }
        _toastMsg.value = "💰 Income saved! KES ${"%,.0f".format(amount)}"
    }

    fun getAccounts(): List<User> = _accounts.value
    fun getSavingsRate(u: User): Int = if (u.income == 0.0) 0 else ((u.income - u.expenses) / u.income * 100).toInt().coerceIn(0,100)
    fun getDaysMoneyLasts(u: User): Int { val d = u.expenses / 30.0; return if (d == 0.0) 0 else (u.balance / d).toInt() }
    fun getProfit(u: User): Double = u.income - u.expenses
    fun getProfitMargin(u: User): Int = if (u.income == 0.0) 0 else ((u.income - u.expenses) / u.income * 100).toInt()
    fun getTotalOwed(u: User): Double = u.customerDebts.sumOf { it.amount }
}
