package com.example.financialehsan

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.financialehsan.navigation.BottomBarRoutes
import com.example.financialehsan.screens.BudgetManagementScreen
import com.example.financialehsan.screens.CostsScreen
import com.example.financialehsan.screens.RemindersScreen
import com.example.financialehsan.screens.RevenuesScreen
import com.example.financialehsan.screens.components.BottomBar
import com.example.financialehsan.screens.components.TopBar
import com.example.financialehsan.ui.theme.FinancialEhsanTheme
import com.example.financialehsan.ui.theme.Primary
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    companion object{
        const val CHANNEL_NAME = "Financial"
    }
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        var initialRoute = BottomBarRoutes.Costs.route
        intent.getBooleanExtra("navigateToReminders",false).also {
            if (it) initialRoute = BottomBarRoutes.Reminder.route
        }

        setContent {



            FinancialEhsanTheme(true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val appState = LocalAppState.current

                    val bottomBarItems = BottomBarRoutes.entries
                    val currentBackstack by appState.navController.currentBackStackEntryAsState()
                    val findRoute =
                        bottomBarItems.find { currentBackstack?.destination?.route == it.route }

                    var navigationIconRotation = remember {
                        Animatable(0f)
                    }

                    val viewModel:MainViewModel = viewModel()

                    val sumOfCosts by viewModel.sumOfCosts.collectAsState()
                    val sumOfRevenues by viewModel.sumOfRevenues.collectAsState()

                    Scaffold(
                        topBar = {
                            AnimatedVisibility(visible = findRoute != null,enter = expandVertically(),exit = shrinkVertically()) {
                                if (findRoute != null) {
                                    TopBar(
                                        iconRotation = navigationIconRotation.value,
                                        routeTitle = findRoute.title,
                                        routeIcon = findRoute.icon,
                                        sumOfCosts = sumOfCosts,
                                        sumOfRevenues = sumOfRevenues
                                    )
                                }
                            }
                        },
                        bottomBar = {
                            AnimatedVisibility(visible = findRoute != null,enter = expandVertically(),exit = shrinkVertically()) {
                                BottomBar(
                                    iconRotation = navigationIconRotation,
                                    currentRoute = currentBackstack?.destination?.route ?: ""
                                )
                            }
                        },
                        containerColor = Primary,

                    ) {
                        NavHost(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                            ,
                            navController = appState.navController,
                            startDestination = initialRoute
                        ) {
                            composable(BottomBarRoutes.Costs.route) {
                                CostsScreen()
                            }
                            composable(BottomBarRoutes.Revenues.route) {
                                RevenuesScreen()
                            }
                            composable(BottomBarRoutes.BudgetManagement.route) {
                                BudgetManagementScreen()
                            }
                            composable(BottomBarRoutes.Reminder.route) {
                                RemindersScreen()
                            }
                        }
                    }
                }
            }
        }
    }
    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_NAME, CHANNEL_NAME, importance).apply {
                description = CHANNEL_NAME
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}
