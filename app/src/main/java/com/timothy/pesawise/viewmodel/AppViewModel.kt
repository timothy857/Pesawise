package com.timothy.pesawise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.timothy.pesawise.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AppViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _toastMsg = MutableStateFlow<String?>(null)
    val toastMsg: StateFlow<String?> = _toastMsg.asStateFlow()

    fun clearToast() { _toastMsg.value = null }

    fun setCurrentUser(user: User) {
        _currentUser.value = user
    }

    private fun mutateUser(transform: (User) -> User) {
        _currentUser.update { user ->
            user?.let {
                val updated = transform(it)
                saveUserToFirestore(updated)
                updated
            }
        }
    }

    private fun saveUserToFirestore(user: User) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("users").document(uid)
                .set(user, SetOptions.merge())
                .addOnFailureListener {
                    viewModelScope.launch(Dispatchers.Main) {
                        _toastMsg.value = "❌ Sync failed: ${it.message}"
                    }
                }
        }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
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

    fun addIncome(amount: Double, category: String, note: String, icon: String, savingsAmount: Double = 0.0) {
        val tx = Transaction(
            id = System.currentTimeMillis(), type = TransactionType.Income,
            amount = amount, category = category, note = note,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            icon = icon
        )
        mutateUser { u ->
            u.copy(
                income = u.income + amount,
                balance = u.balance + (amount - savingsAmount),
                totalSavings = u.totalSavings + savingsAmount,
                transactions = listOf(tx) + u.transactions
            )
        }
        val msg = if (savingsAmount > 0) 
            "💰 Income saved! KES ${"%,.0f".format(amount)} (Saved KES ${"%,.0f".format(savingsAmount)})"
        else 
            "💰 Income saved! KES ${"%,.0f".format(amount)}"
        _toastMsg.value = msg
    }

    fun getSavingsRate(u: User): Int = if (u.income == 0.0) 0 else ((u.income - u.expenses) / u.income * 100).toInt().coerceIn(0,100)
    fun getDaysMoneyLasts(u: User): Int { val d = u.expenses / 30.0; return if (d == 0.0) 0 else (u.balance / d).toInt() }
    fun getProfit(u: User): Double = u.income - u.expenses
    fun getProfitMargin(u: User): Int = if (u.income == 0.0) 0 else ((u.income - u.expenses) / u.income * 100).toInt()
    fun getTotalOwed(u: User): Double = u.customerDebts.sumOf { it.amount }

    fun addGoal(title: String, target: Double, icon: String, colorHex: String, deadline: String) {
        val newGoal = Goal(
            id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
            title = title,
            target = target,
            saved = 0.0,
            icon = icon,
            colorHex = colorHex,
            deadline = deadline
        )
        mutateUser { u ->
            u.copy(goals = u.goals + newGoal)
        }
        _toastMsg.value = "🎯 Goal '$title' added!"
    }

    fun deleteGoal(goalId: Int) {
        mutateUser { u ->
            u.copy(goals = u.goals.filter { it.id != goalId })
        }
        _toastMsg.value = "🗑️ Goal deleted."
    }

    fun updateProfile(fullname: String, email: String, phone: String) {
        mutateUser { u ->
            u.copy(fullname = fullname, email = email, phone = phone)
        }
        _toastMsg.value = "👤 Profile updated successfully!"
    }

    fun addCustomerDebt(name: String, amount: Double, due: String) {
        val newDebt = CustomerDebt(name, amount, due)
        mutateUser { u ->
            u.copy(customerDebts = u.customerDebts + newDebt)
        }
        _toastMsg.value = "💳 Added debt for $name: KES $amount"
    }

    fun addBusinessSale(itemName: String, quantity: Int, buyingPrice: Double, sellingPrice: Double) {
        val newSale = BusinessSale(
            itemName = itemName,
            quantity = quantity,
            buyingPrice = buyingPrice,
            sellingPrice = sellingPrice,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        )
        mutateUser { u ->
            u.copy(businessSales = listOf(newSale) + u.businessSales)
        }
        _toastMsg.value = "📦 Recorded sale of $itemName"
    }

    fun deleteBusinessSale(saleId: Long) {
        mutateUser { u ->
            u.copy(businessSales = u.businessSales.filter { it.id != saleId })
        }
        _toastMsg.value = "🗑️ Sale record deleted."
    }
}
