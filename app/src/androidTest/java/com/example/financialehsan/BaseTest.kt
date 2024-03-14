package com.example.financialehsan

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

open class BaseTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()
}