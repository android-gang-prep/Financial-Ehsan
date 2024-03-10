package com.example.financialehsan.screens.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.financialehsan.LocalAppState
import com.example.financialehsan.navigation.BottomBarRoutes
import com.example.financialehsan.ui.theme.PrimaryVariant
import com.example.financialehsan.ui.theme.diroozFont
import kotlinx.coroutines.launch

@Composable
fun BottomBar(
    iconRotation:Animatable<Float,AnimationVector1D>,
    currentRoute:String,
) {
    val bottomBarItems = BottomBarRoutes.entries
    val appState = LocalAppState.current
    NavigationBar(
        containerColor = PrimaryVariant,
    ) {
        bottomBarItems.forEach {
            NavigationBarItem(
                selected = currentRoute == it.route,
                onClick = {
                    appState.scope.launch {
                        iconRotation.animateTo(
                            iconRotation.value + 360,
                            animationSpec = tween(400)
                        )
                    }
                    appState.navController.navigate(it.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = it.title, fontFamily = diroozFont)
                }, alwaysShowLabel = false
            )
        }
    }
}