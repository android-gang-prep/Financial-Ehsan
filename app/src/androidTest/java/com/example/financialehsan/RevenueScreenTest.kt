package com.example.financialehsan

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
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
import com.example.financialehsan.constants.MainTestTags
import com.example.financialehsan.constants.RevenueTestTags
import com.example.financialehsan.navigation.BottomBarRoutes
import com.example.financialehsan.utils.formatPrice
import org.junit.Test
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RevenueScreenTest : BaseTest() {

    @Before
    fun setup(){
        composeTestRule.onNodeWithText(BottomBarRoutes.Revenues.title).performClick()
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(BottomBarRoutes.Revenues.title)
                .fetchSemanticsNodes()
                .count() == 2
        }
    }

    @OptIn(ExperimentalTestApi::class)
    fun addRevenue(
        revenueDescription:String = "انجام پروژه برنامه نویسی",
        revenueAmount:String = "100000"
    ){
        composeTestRule.onNodeWithTag(RevenueTestTags.AddRevenueFloatingButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(RevenueTestTags.RevenueBottomSheet.tag))
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueAmountField.tag)
            .performTextInput(revenueAmount)
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueDescriptionField.tag)
            .performTextInput(revenueDescription)
        composeTestRule.onNodeWithTag(RevenueTestTags.SubmitRevenueButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(RevenueTestTags.RevenueBottomSheet.tag))
        composeTestRule.waitUntil {
            composeTestRule.onNode(hasText(revenueDescription)).isDisplayed() &&
                    composeTestRule.onNode(hasText(revenueAmount.toLong().formatPrice())).isDisplayed()
        }
    }


    @Test
    fun Stage1_TotalRevenuesAmountInTopBarChangeTest(){
        val revenueAmount1 = 250000
        val revenueAmount2 = 150000
        addRevenue(
            revenueDescription = "تست کل درامد ها 1 در تاپ بار",
            revenueAmount = revenueAmount1.toString()
        )
        addRevenue(
            revenueDescription = "تست کل درامد ها 2 در تاپ بار",
            revenueAmount = revenueAmount2.toString()
        )
        composeTestRule.onNodeWithTag(MainTestTags.TotalRevenuesAmount.tag)
            .assertTextEquals("درامد: ${(revenueAmount1+revenueAmount2).toLong().formatPrice()} تومان")
    }

    @Test
    fun State2_AddRevenueTest() = addRevenue()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun State3_AddCategoryToAddRevenueTest(){
        val category = "دسته بندی تست"

        composeTestRule.onNodeWithTag(RevenueTestTags.AddRevenueFloatingButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(RevenueTestTags.RevenueBottomSheet.tag))
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueNewCategoryButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(RevenueTestTags.RevenueCategoryBottomSheet.tag))
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueCategoryField.tag)
            .performTextInput(category)
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueCategorySubmitButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(RevenueTestTags.RevenueCategoryBottomSheet.tag))
        composeTestRule.onNodeWithText(category).assertIsDisplayed()
        composeTestRule.onNodeWithText(category).performClick()
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueAmountField.tag)
            .performTextInput("20000")
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueDescriptionField.tag)
            .performTextInput("revenue")
        composeTestRule.onNodeWithTag(RevenueTestTags.SubmitRevenueButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(RevenueTestTags.RevenueBottomSheet.tag))
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText(category).isDisplayed()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun State4_DeleteCategoryTest(){
        val category = "دسته بندی برای حذف"

        composeTestRule.onNodeWithTag(RevenueTestTags.AddRevenueFloatingButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(RevenueTestTags.RevenueBottomSheet.tag))
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueCategoriesLazyRow.tag).apply {
            performScrollToIndex(onChildren().fetchSemanticsNodes().size-1)
        }
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueNewCategoryButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(RevenueTestTags.RevenueCategoryBottomSheet.tag))
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueCategoryField.tag)
            .performTextInput(category)
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueCategorySubmitButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(RevenueTestTags.RevenueCategoryBottomSheet.tag))
        composeTestRule.onNodeWithText(category).assertIsDisplayed()
        composeTestRule.onNodeWithText(category).performTouchInput { longClick() }
        composeTestRule.waitUntilDoesNotExist(hasText(category))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun State5_EditRevenueTest(){
        val category2 = "دسته بندی تغییر یافته"

        val description1 = "درامد فلان"
        val title2 = "درامد تغییر یافته"
        val revenueAmount1 = (1000..Int.MAX_VALUE).random()
        val revenueAmount2 = revenueAmount1+ 500

        addRevenue(
            revenueDescription = description1,
            revenueAmount = revenueAmount1.toString()
        )
        composeTestRule.onNodeWithText(description1).performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(RevenueTestTags.RevenueBottomSheet.tag))

        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueNewCategoryButton.tag).performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(RevenueTestTags.RevenueCategoryBottomSheet.tag))
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueCategoryField.tag)
            .performTextInput(category2)
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueCategorySubmitButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(RevenueTestTags.RevenueCategoryBottomSheet.tag))
        composeTestRule.onNodeWithText(category2)
            .performClick()
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueAmountField.tag).apply {
            performTextClearance()
            performTextInput(revenueAmount2.toString())
        }
        composeTestRule.onNodeWithTag(RevenueTestTags.RevenueDescriptionField.tag)
            .apply {
                performTextClearance()
                performTextInput(title2)
            }
        composeTestRule.onNodeWithTag(RevenueTestTags.SubmitRevenueButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(RevenueTestTags.RevenueBottomSheet.tag))
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText(title2).isDisplayed() &&
                    composeTestRule.onNodeWithText(revenueAmount2.toLong().formatPrice()).isDisplayed() &&
                    composeTestRule.onNodeWithText(category2).isDisplayed()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun State6_DeleteRevenueTest(){
        val revenueDescription = "درامد ای برای تست حذف"
        addRevenue(
            revenueDescription = revenueDescription,
            revenueAmount = "900000"
        )
        composeTestRule
            .onNodeWithText(revenueDescription)
            .performTouchInput { longClick() }
        composeTestRule.waitUntilDoesNotExist(hasText(revenueDescription))
    }


}