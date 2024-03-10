package com.example.financialehsan.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.financialehsan.R

val diroozFont = FontFamily(listOf(
    Font(R.font.dirooz)
))
val defaultTypography =Typography()
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = diroozFont),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = diroozFont),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = diroozFont),
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = diroozFont),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = diroozFont),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = diroozFont),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = diroozFont),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = diroozFont),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = diroozFont),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = diroozFont),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = diroozFont),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = diroozFont),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = diroozFont),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = diroozFont),
)
