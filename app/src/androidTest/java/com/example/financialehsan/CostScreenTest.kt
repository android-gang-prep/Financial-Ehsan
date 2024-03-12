package com.example.financialehsan

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.financialehsan.utils.formatPrice
import org.junit.Rule
import org.junit.Test


class CostScreenTest : BaseTest() {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun AddCostTest(){

        val costDescription = "خرید اینترنت از مخابرات"
        val costAmount = "100000"

        composeTestRule.onNodeWithTag("add_cost_button")
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("cost_sheet"))
        composeTestRule.onNodeWithTag("cost_amount_field")
            .performTextInput(costAmount)
        composeTestRule.onNodeWithTag("cost_description_field")
            .performTextInput(costDescription)
        composeTestRule.onNodeWithTag("submit_cost_button")
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag("cost_sheet"))
        composeTestRule.waitUntil {
            composeTestRule.onNode(hasText(costDescription)).isDisplayed() &&
            composeTestRule.onNode(hasText(costAmount.toLong().formatPrice())).isDisplayed()
        }
    }

}