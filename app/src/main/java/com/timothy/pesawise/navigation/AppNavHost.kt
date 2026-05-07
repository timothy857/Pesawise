package com.timothy.pesawise.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.timothy.pesawise.ui.theme.screens.GetStarted.GetStartedScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.AddBusinessExpenseScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.BusinessAccount
import com.timothy.pesawise.ui.theme.screens.Maindashboard.SalaryEarnerAccount
import com.timothy.pesawise.ui.theme.screens.Maindashboard.StudentAccount
import com.timothy.pesawise.ui.theme.screens.Registerscreen.BusinessRegisterScreen
import com.timothy.pesawise.ui.theme.screens.Registerscreen.RegistrationDashboard
import com.timothy.pesawise.ui.theme.screens.Registerscreen.StudentRegisterscreen
import com.timothy.pesawise.ui.theme.screens.Registerscreen.SalaryRegisterScreen
import com.timothy.pesawise.ui.theme.screens.SplashScreen.SplashScreen
import com.timothy.pesawise.ui.theme.screens.loginscreen.LoginScreen
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timothy.pesawise.models.AccountType
import com.timothy.pesawise.models.User
import androidx.compose.ui.graphics.Color

import androidx.lifecycle.viewmodel.compose.viewModel
import com.timothy.pesawise.ui.theme.screens.Maindashboard.AddExpenseScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.AddIncomeScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.GoalsScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.HistoryScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.ReportsScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.ProfileScreen
import com.timothy.pesawise.viewmodel.AppViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_SPLASH,
    viewModel: AppViewModel = viewModel()
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination
    ) {
        composable(ROUTE_SPLASH) {
            SplashScreen(navController)
        }
        composable(ROUTE_GET_STARTED) {
            GetStartedScreen(navController)
        }
        composable(ROUTE_LOGIN) {
            LoginScreen(navController, viewModel = viewModel)
        }
        composable(ROUTE_REG_DASHBOARD) {
            RegistrationDashboard(navController)
        }
        composable(ROUTE_STUDENT_REGISTER) {
            StudentRegisterscreen(navController, viewModel = viewModel)
        }
        composable(ROUTE_BUSINESS_REGISTER) {
            BusinessRegisterScreen(navController, viewModel = viewModel)
        }
        composable(ROUTE_SALARY_REGISTER) {
            SalaryRegisterScreen(navController, viewModel = viewModel)
        }
        composable(ROUTE_SALARY_DASHBOARD) {
            SalaryEarnerAccount(navController, viewModel = viewModel)
        }
        composable(ROUTE_BUSINESS_DASHBOARD) {
            BusinessAccount(navController, viewModel = viewModel)
        }
        composable(ROUTE_STUDENT_DASHBOARD) {
            StudentAccount(navController, viewModel = viewModel)
        }
        composable(ROUTE_ADD_BUSINESS_EXPENSE) {
            AddBusinessExpenseScreen(navController, viewModel = viewModel)
        }

        // Main App Routes
        composable(ROUTE_DASHBOARD) {
            val u = user ?: return@composable
            when (u.type) {
                AccountType.Business -> BusinessAccount(navController = navController, viewModel = viewModel)
                AccountType.Student  -> StudentAccount(navController = navController, viewModel = viewModel)
                else                 -> SalaryEarnerAccount(navController = navController, viewModel = viewModel)
            }
        }

        composable(ROUTE_ADD_EXPENSE) {
            AddExpenseScreen(
                vm          = viewModel,
                onBack      = { navController.popBackStack() },
                onSaved     = { navController.popBackStack() }
            )
        }

        composable(ROUTE_ADD_INCOME) {
            val u = user ?: return@composable
            val accentColor = when (u.type) {
                AccountType.Business -> Color(0xFF6C5CE7)
                AccountType.Student  -> Color(0xFF0984E3)
                else                 -> Color(0xFF00B894)
            }
            AddIncomeScreen(
                user        = u,
                vm          = viewModel,
                accentColor = accentColor,
                onBack      = { navController.popBackStack() },
                onSaved     = { navController.popBackStack() }
            )
        }

        composable(ROUTE_REPORTS) {
            val u = user ?: return@composable
            val (accentColor, startColor, endColor) = getColorsForUser(u.type)
            ReportsScreen(
                user        = u,
                accentColor = accentColor,
                startColor  = startColor,
                endColor    = endColor,
                onBack      = { navController.popBackStack() }
            )
        }

        composable(ROUTE_GOALS) {
            val u = user ?: return@composable
            val (accentColor, startColor, endColor) = getColorsForUser(u.type)
            GoalsScreen(
                user        = u,
                accentColor = accentColor,
                startColor  = startColor,
                endColor    = endColor,
                onBack      = { navController.popBackStack() }
            )
        }

        composable(ROUTE_HISTORY) {
            val u = user ?: return@composable
            val (accentColor, startColor, endColor) = getColorsForUser(u.type)
            HistoryScreen(
                user        = u,
                accentColor = accentColor,
                startColor  = startColor,
                endColor    = endColor,
                onBack      = { navController.popBackStack() }
            )
        }

        composable(ROUTE_PROFILE) {
            val u = user ?: return@composable
            val (accentColor, startColor, endColor) = getColorsForUser(u.type)
            ProfileScreen(
                user        = u,
                vm          = viewModel,
                accentColor = accentColor,
                startColor  = startColor,
                endColor    = endColor,
                onBack      = { navController.popBackStack() },
                onLogout    = {
                    viewModel.logout()
                    navController.navigate(ROUTE_LOGIN) { popUpTo(0) }
                }
            )
        }
    }
}

fun getColorsForUser(type: AccountType): Triple<Color, Color, Color> {
    return when (type) {
        AccountType.Business -> Triple(Color(0xFF6C5CE7), Color(0xFF6C5CE7), Color(0xFF5B4CC4))
        AccountType.Student  -> Triple(Color(0xFF0984E3), Color(0xFF0984E3), Color(0xFF0873C4))
        else                 -> Triple(Color(0xFF00B894), Color(0xFF00B894), Color(0xFF009B7B))
    }
}

