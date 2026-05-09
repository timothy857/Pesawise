package com.timothy.pesawise.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.timothy.pesawise.models.MealBudget
import com.timothy.pesawise.models.SaveChallenge
import com.timothy.pesawise.models.Student
import com.timothy.pesawise.models.Transaction

interface StudentRepository {
    fun updateSemesterBudget(userId: String, total: Double, spent: Double, onComplete: (Boolean) -> Unit)
    fun updateMealBudget(userId: String, mealBudget: MealBudget, onComplete: (Boolean) -> Unit)
    fun updateSaveChallenge(userId: String, saveChallenge: SaveChallenge, onComplete: (Boolean) -> Unit)
    fun addStudentTransaction(userId: String, transaction: Transaction, onComplete: (Boolean) -> Unit)
    fun getStudentData(userId: String, onDataChange: (Student?) -> Unit)
}

class StudentRepositoryImpl : StudentRepository {
    private val db = FirebaseFirestore.getInstance()

    override fun updateSemesterBudget(userId: String, total: Double, spent: Double, onComplete: (Boolean) -> Unit) {
        val updates = mapOf(
            "semesterBudget" to total,
            "semesterSpent" to spent
        )
        db.collection("users").document(userId).set(updates, SetOptions.merge())
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }

    override fun updateMealBudget(userId: String, mealBudget: MealBudget, onComplete: (Boolean) -> Unit) {
        db.collection("users").document(userId).set(mapOf("mealBudget" to mealBudget), SetOptions.merge())
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }

    override fun updateSaveChallenge(userId: String, saveChallenge: SaveChallenge, onComplete: (Boolean) -> Unit) {
        db.collection("users").document(userId).set(mapOf("saveChallenge" to saveChallenge), SetOptions.merge())
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }

    override fun addStudentTransaction(userId: String, transaction: Transaction, onComplete: (Boolean) -> Unit) {
        db.collection("users").document(userId)
            .update("transactions", FieldValue.arrayUnion(transaction))
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }

    override fun getStudentData(userId: String, onDataChange: (Student?) -> Unit) {
        db.collection("users").document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                onDataChange(null)
                return@addSnapshotListener
            }
            try {
                val student = snapshot?.toObject(Student::class.java)
                onDataChange(student)
            } catch (e: Exception) {
                onDataChange(null)
            }
        }
    }
}
