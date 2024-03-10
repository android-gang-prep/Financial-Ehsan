package com.example.financialehsan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope

class AppState(
    val navController:NavHostController,
    val scope:CoroutineScope
) {
}

@Composable
fun rememberAppState():AppState {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    return remember {
        AppState(
            navController = navController,
            scope = scope
        )
    }
}

val LocalAppState = staticCompositionLocalOf<AppState> { error("No State Provided Yet!") }