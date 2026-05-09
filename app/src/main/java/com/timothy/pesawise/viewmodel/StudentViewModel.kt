package com.timothy.pesawise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.timothy.pesawise.data.StudentRepository
import com.timothy.pesawise.data.StudentRepositoryImpl
import com.timothy.pesawise.models.MealBudget
import com.timothy.pesawise.models.SaveChallenge
import com.timothy.pesawise.models.Student
import com.timothy.pesawise.models.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StudentViewModel(
    private val repository: StudentRepository = StudentRepositoryImpl()
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val userId: String? get() = auth.currentUser?.uid

    private val _student = MutableStateFlow<Student?>(null)
    val student: StateFlow<Student?> = _student.asStateFlow()

    val transactions: StateFlow<List<Transaction>> = _student
        .map { it?.transactions ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        fetchStudentData()
    }

    private fun fetchStudentData() {
        userId?.let { id ->
            repository.getStudentData(id) { data ->
                _student.value = data
            }
        }
    }

    fun updateSemesterBudget(total: Double, spent: Double) {
        userId?.let { id ->
            repository.updateSemesterBudget(id, total, spent) { success ->
                // Handle success/failure
            }
        }
    }

    fun updateMealBudget(daily: Double, spent: Double, days: Int) {
        userId?.let { id ->
            val mealBudget = MealBudget(daily, spent, days)
            repository.updateMealBudget(id, mealBudget) { success ->
                // Handle success/failure
            }
        }
    }

    fun updateSaveChallenge(current: Int, target: Int, perDay: Double) {
        userId?.let { id ->
            val challenge = SaveChallenge(current, target, perDay)
            repository.updateSaveChallenge(id, challenge) { success ->
                // Handle success/failure
            }
        }
    }

    fun addTransaction(transaction: Transaction) {
        userId?.let { id ->
            repository.addStudentTransaction(id, transaction) { success ->
                // Handle success/failure
            }
        }
    }
}
