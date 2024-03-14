package com.example.financialehsan

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
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
import com.example.financialehsan.constants.BudgetTestTags
import com.example.financialehsan.constants.CostTestTags
import com.example.financialehsan.navigation.BottomBarRoutes
import com.example.financialehsan.utils.formatPrice
import org.junit.Test
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.JVM)
class BudgetManagementScreenTest : BaseTest() {

    @Before
    fun setup() {
        composeTestRule.onNodeWithText(BottomBarRoutes.BudgetManagement.title).performClick()
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(BottomBarRoutes.BudgetManagement.title)
                .fetchSemanticsNodes()
                .count() == 2
        }
    }

    @OptIn(ExperimentalTestApi::class)
    fun addBudget(
        budgetAmount: String = "50000"
    ) {
        composeTestRule.onNodeWithTag(BudgetTestTags.AddBudgetFloatingButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(BudgetTestTags.BudgetBottomSheet.tag))
        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetAmountField.tag)
            .performTextInput(budgetAmount)
        composeTestRule.onNodeWithTag(BudgetTestTags.SubmitBudgetButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(BudgetTestTags.BudgetBottomSheet.tag),5000)
        composeTestRule.waitUntil {
            composeTestRule.onNode(hasText(budgetAmount.toLong().formatPrice())).isDisplayed()
        }
    }

    @Test
    fun Stage1_AddBudgetTest() = addBudget()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun Stage2_AddCategoryToAddBudgetTest() {
        val category = "دسته بندی تست"

        composeTestRule.onNodeWithTag(BudgetTestTags.AddBudgetFloatingButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(BudgetTestTags.BudgetBottomSheet.tag))
        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetNewCategoryButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(BudgetTestTags.BudgetCategoryBottomSheet.tag))
        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetCategoryField.tag)
            .performTextInput(category)
        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetCategorySubmitButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(BudgetTestTags.BudgetCategoryBottomSheet.tag))
        composeTestRule.onNodeWithText(category).assertIsDisplayed()
        composeTestRule.onNodeWithText(category).performClick()
        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetAmountField.tag)
            .performTextInput("20000")
        composeTestRule.onNodeWithTag(BudgetTestTags.SubmitBudgetButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(BudgetTestTags.BudgetBottomSheet.tag))
        composeTestRule.waitUntil() {
            composeTestRule.onNodeWithText("بودجه برای $category").isDisplayed()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun Stage3_DeleteCategoryTest() {
        val category = "دسته بندی برای حذف"

        composeTestRule.onNodeWithTag(BudgetTestTags.AddBudgetFloatingButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(BudgetTestTags.BudgetBottomSheet.tag))
        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetCategoriesLazyRow.tag).apply {
            performScrollToIndex(onChildren().fetchSemanticsNodes().size - 1)
        }
        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetNewCategoryButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(BudgetTestTags.BudgetCategoryBottomSheet.tag))
        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetCategoryField.tag)
            .performTextInput(category)
        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetCategorySubmitButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(BudgetTestTags.BudgetCategoryBottomSheet.tag))
        composeTestRule.onNodeWithText(category).assertIsDisplayed()
        composeTestRule.onNodeWithText(category).performTouchInput { longClick() }
        composeTestRule.waitUntilDoesNotExist(hasText(category))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun Stage4_EditBudgetTest() {
        val category2 = "دسته بندی تغییر یافته"

        val budgetAmount1 = (1000..Int.MAX_VALUE).random()
        val budgetAmount2 = budgetAmount1 + 500

        addBudget(
            budgetAmount = budgetAmount1.toString()
        )
        composeTestRule.onNodeWithText(budgetAmount1.toLong().formatPrice()).performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(BudgetTestTags.BudgetBottomSheet.tag))

        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetNewCategoryButton.tag).performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(BudgetTestTags.BudgetCategoryBottomSheet.tag))
        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetCategoryField.tag)
            .performTextInput(category2)
        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetCategorySubmitButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(BudgetTestTags.BudgetCategoryBottomSheet.tag))
        composeTestRule.onNodeWithText(category2)
            .performClick()
        composeTestRule.onNodeWithTag(BudgetTestTags.BudgetAmountField.tag).apply {
            performTextClearance()
            performTextInput(budgetAmount2.toString())
        }
        composeTestRule.onNodeWithTag(BudgetTestTags.SubmitBudgetButton.tag)
            .performClick()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(BudgetTestTags.BudgetBottomSheet.tag))
        composeTestRule.waitUntil {
                    composeTestRule.onNodeWithText(budgetAmount2.toLong().formatPrice())
                        .isDisplayed() &&
                    composeTestRule.onNodeWithText("بودجه برای $category2").isDisplayed()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun Stage5_DeleteBudgetTest() {
        val budgetAmount = 9000000
        addBudget(
            budgetAmount = budgetAmount.toString()
        )
        composeTestRule
            .onNodeWithText(budgetAmount.toLong().formatPrice())
            .performTouchInput { longClick() }
        composeTestRule.waitUntilDoesNotExist(hasText(budgetAmount.toLong().formatPrice()))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun Stage6_AddCostWarningWhenCostAmountIsHigherThanBudgetTest(){
        val budgetAmount = 100000
        addBudget(
            budgetAmount = budgetAmount.toString()
        )
        composeTestRule.onNodeWithText(BottomBarRoutes.Costs.title)
            .performClick()
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(BottomBarRoutes.Costs.title)
                .fetchSemanticsNodes()
                .count() == 2
        }

        val costDescription = "تست محدودیت بودجه"
        composeTestRule.onNodeWithTag(CostTestTags.AddCostFloatingButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(CostTestTags.CostBottomSheet.tag))
        composeTestRule.onNodeWithTag(CostTestTags.CostAmountField.tag)
            .performTextInput((budgetAmount+1).toString())
        composeTestRule.onNodeWithTag(CostTestTags.CostDescriptionField.tag)
            .performTextInput(costDescription)
        composeTestRule.onNodeWithTag(CostTestTags.SubmitCostButton.tag)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(CostTestTags.BudgetLimitBottomSheet.tag))
        composeTestRule.onNodeWithTag(CostTestTags.BudgetLimitBottomSheetApplyButton.tag)
            .performClick()
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithTag(CostTestTags.BudgetLimitBottomSheet.tag).isNotDisplayed() &&
            composeTestRule.onNodeWithTag(CostTestTags.CostBottomSheet.tag).isNotDisplayed()
        }
        composeTestRule.waitUntilAtLeastOneExists(hasText(costDescription))
    }


}