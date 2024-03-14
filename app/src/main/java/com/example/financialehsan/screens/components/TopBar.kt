package com.example.financialehsan.screens.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financialehsan.constants.MainTestTags
import com.example.financialehsan.ui.theme.Green
import com.example.financialehsan.ui.theme.PrimaryVariant
import com.example.financialehsan.ui.theme.Red
import com.example.financialehsan.ui.theme.diroozFont
import com.example.financialehsan.utils.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    iconRotation:Float,
    routeTitle:String,
    routeIcon:Int,
    sumOfCosts:Int,
    sumOfRevenues:Int
) {
    TopAppBar(
        title = {
            AnimatedContent(targetState = routeTitle) {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }, colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = PrimaryVariant
        ),
        navigationIcon = {
            Box(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 8.dp
                )
            ) {
                Icon(
                    modifier = Modifier.graphicsLayer {
                        rotationZ = iconRotation
                    },
                    painter = painterResource(id = routeIcon),
                    contentDescription = null
                )
            }
        },
        actions = {
            Column(modifier=Modifier.padding(horizontal = 12.dp)) {
                AnimatedContent(targetState = sumOfCosts.toLong().formatPrice()) {
                    Text(text = "هزینه ها: $it تومان", fontSize = 12.sp, color = Red,modifier=Modifier.testTag(MainTestTags.TotalCostsAmount.tag))
                }
                Spacer(modifier = Modifier.height(2.dp))
                AnimatedContent(targetState = sumOfRevenues.toLong().formatPrice()) {
                    Text(text = "درامد: $it تومان", fontSize = 12.sp, color = Green,modifier=Modifier.testTag(MainTestTags.TotalRevenuesAmount.tag))
                }
            }
        }

    )
}