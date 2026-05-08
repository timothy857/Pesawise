package com.timothy.pesawise.data

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.timothy.pesawise.navigation.ROUTE_LOGIN
import com.timothy.pesawise.navigation.ROUTE_SALARY_DASHBOARD
import com.timothy.pesawise.navigation.ROUTE_BUSINESS_DASHBOARD
import com.timothy.pesawise.navigation.ROUTE_STUDENT_DASHBOARD

import com.timothy.pesawise.models.AccountType
import com.timothy.pesawise.models.User
import com.timothy.pesawise.viewmodel.AppViewModel

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

    fun login(email: String, password: String, appViewModel: AppViewModel) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid
                    if (userId != null) {
                        // Fetch user data from database
                        mDatabase.getReference("users").child(userId)
                            .get().addOnSuccessListener { snapshot ->
                                val fullname = snapshot.child("fullname").value as? String ?: "User"
                                val accountTypeStr = snapshot.child("accountType").value as? String ?: "Salaried"
                                val emailAddr = snapshot.child("email").value as? String ?: email
                                
                                val accountType = when (accountTypeStr) {
                                    "Business" -> AccountType.Business
                                    "Student" -> AccountType.Student
                                    else -> AccountType.Salaried
                                }

                                // Update AppViewModel with the logged-in user
                                val loggedInUser = User(
                                    name = fullname,
                                    email = emailAddr,
                                    password = "", // Don't store password locally
                                    type = accountType
                                )
                                appViewModel.setCurrentUser(loggedInUser)

                                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                
                                when (accountType) {
                                    AccountType.Business -> {
                                        navController.navigate(ROUTE_BUSINESS_DASHBOARD) {
                                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                                        }
                                    }
                                    AccountType.Student -> {
                                        navController.navigate(ROUTE_STUDENT_DASHBOARD) {
                                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                                        }
                                    }
                                    AccountType.Salaried -> {
                                        navController.navigate(ROUTE_SALARY_DASHBOARD) {
                                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                                        }
                                    }
                                }
                            }.addOnFailureListener {
                                Toast.makeText(context, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
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
