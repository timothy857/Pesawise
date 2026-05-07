package com.timothy.pesawise.data

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.timothy.pesawise.navigation.ROUTE_DASHBOARD
import com.timothy.pesawise.navigation.ROUTE_LOGIN
import com.timothy.pesawise.navigation.ROUTE_SALARY_DASHBOARD
import com.timothy.pesawise.navigation.ROUTE_BUSINESS_DASHBOARD
import com.timothy.pesawise.navigation.ROUTE_STUDENT_DASHBOARD

class AuthViewModel(var navController: NavHostController, var context: Context) {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun signup(fullname: String, email: String, password: String, accountType: String) {
        if (fullname.isBlank() || email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid
                    if (userId != null) {
                        val userMap = mapOf(
                            "fullname" to fullname,
                            "email" to email,
                            "accountType" to accountType,
                            "userid" to userId
                        )
                        mDatabase.getReference("users").child(userId).setValue(userMap)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show()
                                    navController.navigate(ROUTE_LOGIN)
                                } else {
                                    Toast.makeText(context, "Database error: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid
                    if (userId != null) {
                        // Fetch user type from database
                        mDatabase.getReference("users").child(userId).child("accountType")
                            .get().addOnSuccessListener { snapshot ->
                                val accountType = snapshot.value as? String
                                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                
                                when (accountType) {
                                    "Business" -> {
                                        navController.navigate(ROUTE_BUSINESS_DASHBOARD) {
                                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                                        }
                                    }
                                    "Student" -> {
                                        navController.navigate(ROUTE_STUDENT_DASHBOARD) {
                                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                                        }
                                    }
                                    "Salaried" -> {
                                        navController.navigate(ROUTE_SALARY_DASHBOARD) {
                                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                                        }
                                    }
                                    else -> {
                                        navController.navigate(ROUTE_DASHBOARD) {
                                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                                        }
                                    }
                                }
                            }.addOnFailureListener {
                                // Fallback to default dashboard if database fetch fails
                                navController.navigate(ROUTE_DASHBOARD) {
                                    popUpTo(ROUTE_LOGIN) { inclusive = true }
                                }
                            }
                    }
                } else {
                    Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun logout() {
        mAuth.signOut()
        navController.navigate(ROUTE_LOGIN)
    }

    fun isloggedin(): Boolean {
        return mAuth.currentUser != null
    }
}
