package com.example.financialehsan

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import com.example.financialehsan.constants.CostTestTags
import com.example.financialehsan.constants.MainTestTags
import com.example.financialehsan.utils.formatPrice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class CostScreenTest : BaseTest() {



    @OptIn(ExperimentalTestApi::class)
    fun addCost(
        costDescription:String = "خرید اینترنت از مخابرات",
        costAmount:String = "100000"
    ){
        composeTestRule.onNodeWithTag(CostTestTags.AddCostFloatingButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(CostTestTags.CostBottomSheet.tag))
        composeTestRule.onNodeWithTag(CostTestTags.CostAmountField.tag)
            .performTextInput(costAmount)
        composeTestRule.onNodeWithTag(CostTestTags.CostDescriptionField.tag)
            .performTextInput(costDescription)
        composeTestRule.onNodeWithTag(CostTestTags.SubmitCostButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(CostTestTags.CostBottomSheet.tag), 2000)
        composeTestRule.waitUntil {
            composeTestRule.onNode(hasText(costDescription)).isDisplayed() &&
            composeTestRule.onNode(hasText(costAmount.toLong().formatPrice())).isDisplayed()
        }
    }

    @Test
    fun Stage1_TotalCostsAmountInTopBarChangeTest(){
        val costAmount1 = 50000
        val costAmount2 = 200000
        addCost(
            costDescription = "تست کل هزینه ها 1 در تاپ بار",
            costAmount = costAmount1.toString()
        )
        addCost(
            costDescription = "تست کل هزینه ها 2 در تاپ بار",
            costAmount = costAmount2.toString()
        )
        composeTestRule.onNodeWithTag(MainTestTags.TotalCostsAmount.tag)
            .assertTextEquals("هزینه ها: ${(costAmount1+costAmount2).toLong().formatPrice()} تومان")
    }

    @Test
    fun Stage2_AddCostTest() = addCost()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun Stage3_AddCategoryToAddCostTest(){
        val category = "دسته بندی تست"

        composeTestRule.onNodeWithTag(CostTestTags.AddCostFloatingButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(CostTestTags.CostBottomSheet.tag))
        composeTestRule.onNodeWithTag(CostTestTags.CostNewCategoryButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(CostTestTags.CostCategoryBottomSheet.tag))
        composeTestRule.onNodeWithTag(CostTestTags.CostCategoryField.tag)
            .performTextInput(category)
        composeTestRule.onNodeWithTag(CostTestTags.CostCategorySubmitButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(CostTestTags.CostCategoryBottomSheet.tag))
        composeTestRule.onNodeWithText(category).assertIsDisplayed()
        composeTestRule.onNodeWithText(category).performClick()
        composeTestRule.onNodeWithTag(CostTestTags.CostAmountField.tag)
            .performTextInput("20000")
        composeTestRule.onNodeWithTag(CostTestTags.CostDescriptionField.tag)
            .performTextInput("cost")
        composeTestRule.onNodeWithTag(CostTestTags.SubmitCostButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(CostTestTags.CostBottomSheet.tag), 2000)
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText(category).isDisplayed()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun Stage4_DeleteCategoryTest(){
        val category = "دسته بندی برای حذف"

        composeTestRule.onNodeWithTag(CostTestTags.AddCostFloatingButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(CostTestTags.CostBottomSheet.tag))
        composeTestRule.onNodeWithTag(CostTestTags.CostCategoriesLazyRow.tag).apply {
            performScrollToIndex(onChildren().fetchSemanticsNodes().size-1)
        }
        composeTestRule.onNodeWithTag(CostTestTags.CostNewCategoryButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(CostTestTags.CostCategoryBottomSheet.tag))
        composeTestRule.onNodeWithTag(CostTestTags.CostCategoryField.tag)
            .performTextInput(category)
        composeTestRule.onNodeWithTag(CostTestTags.CostCategorySubmitButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(CostTestTags.CostCategoryBottomSheet.tag))
        composeTestRule.onNodeWithText(category).assertIsDisplayed()
        composeTestRule.onNodeWithText(category).performTouchInput { longClick() }
        composeTestRule.waitUntilDoesNotExist(hasText(category))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun Stage5_EditCostTest(){
        val category2 = "دسته بندی تغییر یافته"

        val description1 = "هزینه فلان"
        val title2 = "هزینه تغییر یافته"
        val costAmount1 = (1000..Int.MAX_VALUE).random()
        val costAmount2 = costAmount1+ 500

        addCost(
            costDescription = description1,
            costAmount = costAmount1.toString()
        )
        composeTestRule.onNodeWithText(description1).performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(CostTestTags.CostBottomSheet.tag))

        composeTestRule.onNodeWithTag(CostTestTags.CostNewCategoryButton.tag).performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(CostTestTags.CostCategoryBottomSheet.tag))
        composeTestRule.onNodeWithTag(CostTestTags.CostCategoryField.tag)
            .performTextInput(category2)
        composeTestRule.onNodeWithTag(CostTestTags.CostCategorySubmitButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(CostTestTags.CostCategoryBottomSheet.tag))
        composeTestRule.onNodeWithText(category2)
            .performClick()
        composeTestRule.onNodeWithTag(CostTestTags.CostAmountField.tag).apply {
            performTextClearance()
            performTextInput(costAmount2.toString())
        }
        composeTestRule.onNodeWithTag(CostTestTags.CostDescriptionField.tag)
            .apply {
                performTextClearance()
                performTextInput(title2)
            }
        composeTestRule.onNodeWithTag(CostTestTags.SubmitCostButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(CostTestTags.CostBottomSheet.tag))
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithText(title2).isDisplayed() &&
            composeTestRule.onNodeWithText(costAmount2.toLong().formatPrice()).isDisplayed() &&
            composeTestRule.onNodeWithText(category2).isDisplayed()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun Stage6_DeleteCostTest(){
        val costDescription = "هزینه ای برای تست حذف"
        addCost(
            costDescription = costDescription,
            costAmount = "900000"
        )
        composeTestRule
            .onNodeWithText(costDescription)
            .performTouchInput { longClick() }
        composeTestRule.waitUntilDoesNotExist(hasText(costDescription))
    }




}