package com.timothy.pesawise.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.timothy.pesawise.models.AccountType
import com.timothy.pesawise.models.User

import com.timothy.pesawise.ui.theme.screens.SplashScreen.SplashScreen
import com.timothy.pesawise.ui.theme.screens.loginscreen.LoginScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.StudentAccount
import com.timothy.pesawise.ui.theme.screens.Maindashboard.BusinessAccount
import com.timothy.pesawise.ui.theme.screens.Maindashboard.SalaryEarnerAccount
import com.timothy.pesawise.ui.theme.screens.Maindashboard.AddExpenseScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.AddIncomeScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.ReportsScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.GoalsScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.HistoryScreen
import com.timothy.pesawise.ui.theme.screens.Maindashboard.ProfileScreen
import com.timothy.pesawise.viewmodel.AppViewModel
import kotlinx.coroutines.delay

// ── Nav graph ─────────────────────────────────────────────
@Composable
fun PesaNavGraph(vm: AppViewModel = viewModel()) {
    val nav = rememberNavController()
    val user by vm.currentUser.collectAsStateWithLifecycle()
    val toast by vm.toastMsg.collectAsStateWithLifecycle()

    // Resolve theme colours for current user
    val accentColor = when (user?.type) {
        AccountType.Business -> Color(0xFF6C5CE7)
        AccountType.Student -> Color(0xFF0984E3)
        else -> Color(0xFF00B894)
    }
    val startColor = when (user?.type) {
        AccountType.Business -> Color(0xFF6C5CE7)
        AccountType.Student -> Color(0xFF0984E3)
        else -> Color(0xFF00B894)
    }
    val endColor = when (user?.type) {
        AccountType.Business -> Color(0xFF5B4CC4)
        AccountType.Student -> Color(0xFF0873C4)
        else -> Color(0xFF009B7B)
    }

    NavHost(navController = nav, startDestination = ROUTE_SPLASH) {

        composable(ROUTE_SPLASH) {
            SplashScreen(navController = nav)
        }

        composable(ROUTE_LOGIN) {
            LoginScreen(navController = nav, viewModel = vm)
        }

        composable(ROUTE_DASHBOARD) {
            val u = user ?: return@composable
            when (u.type) {
                AccountType.Business -> BusinessAccount(navController = nav, viewModel = vm)
                AccountType.Student -> StudentAccount(navController = nav, viewModel = vm)
                else -> SalaryEarnerAccount(navController = nav, viewModel = vm)
            }
        }

        composable(ROUTE_ADD_EXPENSE) {
            AddExpenseScreen(
                vm = vm,
                onBack = { nav.popBackStack() },
                onSaved = { nav.popBackStack() }
            )
        }

        composable(ROUTE_ADD_INCOME) {
            val u = user ?: return@composable
            AddIncomeScreen(
                user = u,
                vm = vm,
                accentColor = accentColor,
                onBack = { nav.popBackStack() },
                onSaved = { nav.popBackStack() }
            )
        }

        composable(ROUTE_REPORTS) {
            val u = user ?: return@composable
            ReportsScreen(
                user = u,
                accentColor = accentColor,
                startColor = startColor,
                endColor = endColor,
                onBack = { nav.popBackStack() }
            )
        }

        composable(ROUTE_GOALS) {
            val u = user ?: return@composable
            GoalsScreen(
                user = u,
                accentColor = accentColor,
                startColor = startColor,
                endColor = endColor,
                onBack = { nav.popBackStack() }
            )
        }

        composable(ROUTE_HISTORY) {
            val u = user ?: return@composable
            HistoryScreen(
                user = u,
                accentColor = accentColor,
                startColor = startColor,
                endColor = endColor,
                onBack = { nav.popBackStack() }
            )
        }

        composable(ROUTE_PROFILE) {
            val u = user ?: return@composable
            ProfileScreen(
                user = u,
                vm = vm,
                accentColor = accentColor,
                startColor = startColor,
                endColor = endColor,
                onBack = { nav.popBackStack() },
                onLogout = {
                    vm.logout()
                    nav.navigate(ROUTE_LOGIN) { popUpTo(0) }
                }
            )
        }
    }

    // Toast
    LaunchedEffect(toast) {
        if (toast != null) {
            delay(2800)
            vm.clearToast()
        }
    }
}

// ── Bottom Nav Bar ────────────────────────────────────────
@Composable
fun PesaBottomNav(
    nav: NavHostController,
    currentRoute: String,
    accentColor: Color,
    primaryColor: Color
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        val items = listOf(
            Triple(ROUTE_DASHBOARD, "🏠", "Home"),
            Triple(ROUTE_REPORTS,   "📊", "Reports"),
            Triple(ROUTE_ADD_EXPENSE,"➕", ""),
            Triple(ROUTE_GOALS,     "🎯", "Goals"),
            Triple(ROUTE_PROFILE,   "👤", "Profile")
        )
        items.forEach { (route, icon, label) ->
            if (route == ROUTE_ADD_EXPENSE) {
                // FAB-style centre button
                NavigationBarItem(
                    selected = false,
                    onClick  = { nav.navigate(route) },
                    icon     = {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(primaryColor),
                            contentAlignment = Alignment.Center
                        ) { Text(icon, fontSize = 20.sp) }
                    },
                    label = {}
                )
            } else {
                NavigationBarItem(
                    selected = currentRoute == route,
                    onClick  = { nav.navigate(route) { launchSingleTop = true } },
                    icon     = { Text(icon, fontSize = 18.sp) },
                    label    = {
                        Text(
                            label,
                            fontSize   = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color      = if (currentRoute == route) accentColor else Color(0xFF94A3B8)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = accentColor
                    )
                )
            }
        }
    }
}
