package com.timothy.pesawise.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.timothy.pesawise.navigation.ROUTE_LOGIN
import com.timothy.pesawise.navigation.ROUTE_SALARY_DASHBOARD
import com.timothy.pesawise.navigation.ROUTE_BUSINESS_DASHBOARD
import com.timothy.pesawise.navigation.ROUTE_STUDENT_DASHBOARD

import com.timothy.pesawise.models.AccountType
import com.timothy.pesawise.models.User
import com.timothy.pesawise.viewmodel.AppViewModel

class AuthViewModel(var navController: NavHostController, var context: Context) {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

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
                        val newUser = User(
                            userid = userId,
                            fullname = fullname,
                            email = email,
                            type = when (accountType) {
                                "Business" -> AccountType.Business
                                "Student" -> AccountType.Student
                                else -> AccountType.Salaried
                            }
                        )
                        
                        mFirestore.collection("users").document(userId).set(newUser)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show()
                                    navController.navigate(ROUTE_LOGIN)
                                } else {
                                    Toast.makeText(context, "Firestore error: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
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
                        mFirestore.collection("users").document(userId).get()
                            .addOnSuccessListener { document ->
                                try {
                                    if (document.exists()) {
                                        val loggedInUser = document.toObject(User::class.java)
                                        if (loggedInUser != null) {
                                            appViewModel.setCurrentUser(loggedInUser)
                                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                            
                                            when (loggedInUser.type) {
                                                AccountType.Business -> navController.navigate(ROUTE_BUSINESS_DASHBOARD) { popUpTo(ROUTE_LOGIN) { inclusive = true } }
                                                AccountType.Student -> navController.navigate(ROUTE_STUDENT_DASHBOARD) { popUpTo(ROUTE_LOGIN) { inclusive = true } }
                                                AccountType.Salaried -> navController.navigate(ROUTE_SALARY_DASHBOARD) { popUpTo(ROUTE_LOGIN) { inclusive = true } }
                                            }
                                        } else {
                                             Toast.makeText(context, "User data is empty", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(context, "No profile found for this user", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e("AuthViewModel", "Error parsing user data", e)
                                    Toast.makeText(context, "Error loading profile: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }.addOnFailureListener {
                                Toast.makeText(context, "Failed to fetch user data: ${it.message}", Toast.LENGTH_SHORT).show()
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

    fun isloggedin(): Boolean = mAuth.currentUser != null
}
