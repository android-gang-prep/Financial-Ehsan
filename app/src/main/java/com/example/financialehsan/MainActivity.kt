package com.example.financialehsan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
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
import com.example.financialehsan.ui.theme.PrimaryVariant
import com.example.financialehsan.ui.theme.diroozFont
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                    Scaffold(
                        topBar = {
                            AnimatedVisibility(visible = findRoute != null,enter = expandVertically(),exit = shrinkVertically()) {
                                if (findRoute != null) {
                                    TopBar(
                                        iconRotation = navigationIconRotation.value,
                                        routeTitle = findRoute.title,
                                        routeIcon = findRoute.icon
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
                            startDestination = BottomBarRoutes.Costs.route
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
}
