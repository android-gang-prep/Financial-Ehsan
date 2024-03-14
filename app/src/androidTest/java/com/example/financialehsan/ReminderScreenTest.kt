package com.example.financialehsan

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import com.example.financialehsan.constants.ReminderTestTags
import com.example.financialehsan.navigation.BottomBarRoutes
import com.example.financialehsan.utils.formatPrice
import org.junit.Test
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ReminderScreenTest : BaseTest() {

    @Before
    fun setup() {
        composeTestRule.onNodeWithText(BottomBarRoutes.Reminder.title).performClick()
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(BottomBarRoutes.Reminder.title)
                .fetchSemanticsNodes()
                .count() == 2
        }
    }

    @OptIn(ExperimentalTestApi::class)
    fun addReminder(
        reminderAmount: String = "50000",
        reminderDescription:String = "پرداخت قبض"
    ) {
        composeTestRule.onNodeWithTag(ReminderTestTags.AddReminderFloatingButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(ReminderTestTags.ReminderBottomSheet.tag))
        composeTestRule.onNodeWithTag(ReminderTestTags.ReminderAmountField.tag)
            .performTextInput(reminderAmount)
        composeTestRule.onNodeWithTag(ReminderTestTags.ReminderDescriptionField.tag)
            .performTextInput(reminderDescription)
        composeTestRule.onNodeWithTag(ReminderTestTags.SubmitReminderButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(ReminderTestTags.ReminderBottomSheet.tag),5000)
        composeTestRule.waitUntil {
            composeTestRule.onNode(hasText(reminderAmount.toLong().formatPrice())).isDisplayed() &&
            composeTestRule.onNode(hasText(reminderDescription)).isDisplayed()
        }
    }

    @Test
    fun Stage1_AddReminderTest() = addReminder()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun Stage2_EditReminderTest() {

        val reminderDescription1 = "پرداخت درحال تست"
        val reminderDescription2 = "پرداخت تستی تغییر یافته"

        val reminderAmount1 = (1000..Int.MAX_VALUE).random()
        val reminderAmount2 = reminderAmount1 + 500

        addReminder(
            reminderAmount = reminderAmount1.toString(),
            reminderDescription = reminderDescription1
        )
        composeTestRule.onNodeWithText(reminderAmount1.toLong().formatPrice()).performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(ReminderTestTags.ReminderBottomSheet.tag))
        composeTestRule.onNodeWithTag(ReminderTestTags.ReminderAmountField.tag).apply {
            performTextClearance()
            performTextInput(reminderAmount2.toString())
        }
        composeTestRule.onNodeWithTag(ReminderTestTags.ReminderDescriptionField.tag).apply {
            performTextClearance()
            performTextInput(reminderDescription2)
        }
        composeTestRule.onNodeWithTag(ReminderTestTags.SubmitReminderButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(ReminderTestTags.ReminderBottomSheet.tag))
        composeTestRule.waitUntil {
                    composeTestRule.onNodeWithText(reminderAmount2.toLong().formatPrice())
                        .isDisplayed() &&
                    composeTestRule.onNodeWithText(reminderDescription2)
                        .isDisplayed()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun Stage3_DeleteReminderTest() {
        val description = "اجاره خونه"
        addReminder(
            reminderDescription = description,
            reminderAmount = "230000"
        )
        composeTestRule
            .onNodeWithText(description)
            .performTouchInput { longClick() }
        composeTestRule.waitUntilDoesNotExist(hasText(description))
    }


}