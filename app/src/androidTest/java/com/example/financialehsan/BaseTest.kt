package com.example.financialehsan

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule

open class BaseTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()
}