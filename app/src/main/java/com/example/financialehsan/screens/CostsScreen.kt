package com.example.financialehsan.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financialehsan.viewModels.CostViewModel
import com.example.financialehsan.LocalAppState
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.CostCategory
import com.example.financialehsan.database.entities.relations.CostWithCategory
import com.example.financialehsan.defaultCostCategories
import com.example.financialehsan.screens.components.AppTextField
import com.example.financialehsan.screens.components.BorderButton
import com.example.financialehsan.screens.components.SelectableButton
import com.example.financialehsan.ui.theme.PrimaryVariant
import com.example.financialehsan.ui.theme.Red
import com.example.financialehsan.ui.theme.diroozFont
import com.example.financialehsan.constants.CostTestTags
import com.example.financialehsan.utils.formatPrice
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

data class LimitSheetData(
    val cost: Cost,
    val categoryTitle:String,
    val budgetAmount: Long,
    val costAmount: Long,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CostsScreen(viewModel: CostViewModel = koinViewModel()) {
    val appState = LocalAppState.current
    val costs by viewModel.costs.collectAsState()
    val budgets by viewModel.budgets.collectAsState()
    val categories by viewModel.categories.collectAsState()

    val costSheetOpen = remember {
        mutableStateOf(false)
    }
    val newCategorySheetOpen = remember {
        mutableStateOf(false)
    }
    val budgetLimitSheetOpen = remember {
        mutableStateOf(false)
    }
    val budgetSheetData = remember {
        mutableStateOf<LimitSheetData?>(null)
    }

    val selectedCost = remember {
        mutableStateOf<CostWithCategory?>(null)
    }

    val costSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val categorySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val budgetLimitSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (costSheetOpen.value) {
        val editMode = selectedCost.value != null
        val costAmount = remember {
            mutableStateOf(selectedCost.value?.cost?.amount?.toString().orEmpty())
        }
        val costDescription = remember {
            mutableStateOf(selectedCost.value?.cost?.description.orEmpty())
        }
        val selectedCategory = remember {
            mutableIntStateOf(
                selectedCost.value?.category?.id ?: (categories.firstOrNull()?.id ?: 0)
            )
        }
        ModalBottomSheet(
            modifier=Modifier.testTag(CostTestTags.CostBottomSheet.tag),
            onDismissRequest = {
                costSheetOpen.value = false
            },
            sheetState = costSheetState,
            containerColor = PrimaryVariant,
            shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Column(
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp, start = 16.dp)
                        .navigationBarsPadding()
                ) {
                    Text(
                        text = if (!editMode) "اضافه کردن هزینه" else "ویرایش هزینه",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(
                        modifier=Modifier.testTag(CostTestTags.CostCategoriesLazyRow.tag),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(categories) {
                            SelectableButton(
                                text = it.title,
                                selected = it.id == selectedCategory.intValue,
                                onSelect = {
                                    selectedCategory.intValue = it.id
                                },
                                onLongClick = {
                                    if (it.title !in defaultCostCategories && it.id != selectedCategory.intValue) {
                                        viewModel.deleteCategory(it)
                                    }
                                }
                            )
                        }
                        item {
                            BorderButton(onClick = {
                                newCategorySheetOpen.value = true
                            }, testTag =  CostTestTags.CostNewCategoryButton.tag) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    AppTextField(
                        modifier = Modifier.fillMaxWidth(),
                        testTag = CostTestTags.CostAmountField.tag,
                        value = costAmount.value,
                        onValueChange = {
                            costAmount.value = it
                        },
                        placeholder = "مقدار هزینه (تومان)",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AppTextField(
                        modifier = Modifier.fillMaxWidth(),
                        testTag = CostTestTags.CostDescriptionField.tag,
                        height = 200.dp,
                        value = costDescription.value,
                        onValueChange = {
                            costDescription.value = it
                        },
                        placeholder = "توضیحات هزینه"
                    )
                    Spacer(modifier = Modifier.heightIn(22.dp))
                    Button(onClick = {
                        if (costDescription.value.isNotBlank() && costAmount.value.toLongOrNull() != null) {
                            val totalCostsForThisCategory = costs
                                .filter { it.category?.id == selectedCategory.intValue }
                                .sumOf { it.cost.amount }
                            val totalCostsAmountForThisCategory = if(editMode){
                                totalCostsForThisCategory + (costAmount.value.toLong() - ((costs.find { it.cost.id == selectedCost.value?.cost?.id })?.cost?.amount
                                    ?: 0))
                            }else{
                                totalCostsForThisCategory + costAmount.value.toLong()
                            }
                            val budgetForThisCategory = budgets
                                .filter {
                                    it.category.id == selectedCategory.intValue
                                }
                            val budgetAmountForThisCategory = if(budgetForThisCategory.isEmpty()){
                                Long.MAX_VALUE
                            }else{
                                budgetForThisCategory.sumOf { it.budget.amount }
                            }
                            val costToUpsert = if (editMode){
                                selectedCost.value!!.cost.copy(
                                    categoryId = selectedCategory.intValue,
                                    description = costDescription.value,
                                    amount = costAmount.value.toLong()
                                )
                            }else{
                                Cost(
                                    categoryId = selectedCategory.intValue,
                                    description = costDescription.value,
                                    amount = costAmount.value.toLong()
                                )
                            }
                            if (totalCostsAmountForThisCategory > budgetAmountForThisCategory) {
                                budgetSheetData.value = LimitSheetData(
                                    cost = costToUpsert ,
                                    budgetAmount = budgetAmountForThisCategory,
                                    costAmount = totalCostsAmountForThisCategory,
                                    categoryTitle = categories.find { it.id == selectedCategory.intValue }?.title ?: ""
                                )
                                budgetLimitSheetOpen.value = true
                            }else{
                                if (editMode) {
                                    viewModel.updateCost(costToUpsert)
                                    appState.scope.launch {
                                        costSheetState.hide()
                                    }
                                        .invokeOnCompletion {
                                            costSheetOpen.value = false
                                        }
                                } else {
                                    viewModel.addCost(costToUpsert)
                                    appState.scope.launch {
                                        costSheetState.hide()
                                    }
                                        .invokeOnCompletion {
                                            costSheetOpen.value = false
                                        }
                                }
                            }


                        }

                    }, modifier = Modifier.fillMaxWidth().testTag(CostTestTags.SubmitCostButton.tag), shape = RoundedCornerShape(8.dp)) {
                        Text(text = if (!editMode) "اضافه کردن" else "ویرایش")
                    }
                }
            }
        }
    }
    if (newCategorySheetOpen.value) {
        var categoryTitle = remember {
            mutableStateOf("")
        }
        ModalBottomSheet(
            modifier=Modifier.testTag(CostTestTags.CostCategoryBottomSheet.tag),
            onDismissRequest = {
                newCategorySheetOpen.value = false
            },
            sheetState = categorySheetState,
            containerColor = PrimaryVariant,
            shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Column(
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp, start = 16.dp)
                        .navigationBarsPadding()
                ) {
                    Text(text = "دسته بندی جدید", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    AppTextField(
                        value = categoryTitle.value,
                        onValueChange = { categoryTitle.value = it },
                        placeholder = "عنوان دسته بندی",
                        testTag = CostTestTags.CostCategoryField.tag
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    Button(onClick = {
                        if (categoryTitle.value.isNotEmpty()) {
                            appState.scope.launch {
                                viewModel.addCategory(categoryTitle.value)
                                categorySheetState.hide()
                            }.invokeOnCompletion {
                                newCategorySheetOpen.value = false
                            }
                        }
                    }, modifier = Modifier.fillMaxWidth().testTag(CostTestTags.CostCategorySubmitButton.tag), shape = RoundedCornerShape(8.dp)) {
                        Text(text = "اضافه کردن")
                    }
                }
            }
        }
    }
    if (budgetLimitSheetOpen.value) {
        val editMode = selectedCost.value != null
        ModalBottomSheet(
            modifier=Modifier.testTag(CostTestTags.BudgetLimitBottomSheet.tag),
            onDismissRequest = {
                budgetLimitSheetOpen.value = false
            },
            sheetState = budgetLimitSheetState,
            containerColor = PrimaryVariant,
            shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Column(
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp, start = 16.dp)
                        .navigationBarsPadding()
                ) {
                    Text(text = "مقدار کل هزینه های دسته بندی ${budgetSheetData.value?.categoryTitle} ( ${budgetSheetData.value?.costAmount?.formatPrice()} تومان ) از بودجه تعیین شده ( ${budgetSheetData.value?.budgetAmount?.formatPrice()} تومان ) بیشتر است، آیا همچنان مایل به ${if (editMode) "ویرایش" else "اضافه"} کردن هزینه هستید؟")
                    Spacer(modifier = Modifier.height(22.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (editMode){
                                    viewModel.updateCost(budgetSheetData.value!!.cost)
                                }else{
                                    viewModel.addCost(budgetSheetData.value!!.cost)
                                }
                                appState.scope.launch {
                                    budgetLimitSheetState.hide()
                                }.invokeOnCompletion {
                                    budgetLimitSheetOpen.value = false
                                }
                                appState.scope.launch {
                                    costSheetState.hide()
                                }.invokeOnCompletion {
                                    costSheetOpen.value = false
                                }
                            }, shape = RoundedCornerShape(8.dp), modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .testTag(CostTestTags.BudgetLimitBottomSheetApplyButton.tag)
                        ) {
                            Text(text = if (editMode) "ویرایش کن" else "اضافه کن")
                        }
                        Button(
                            onClick = {
                                appState.scope.launch {
                                    budgetLimitSheetState.hide()
                                }.invokeOnCompletion {
                                    budgetLimitSheetOpen.value = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Red
                            )
                        ) {
                            Text(text = "بیخیال", color = Color.White)
                        }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.BottomStart
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(costs) { index, cost ->
                CostItem(index = index, cost = cost.cost, category = cost.category, onClick = {
                    selectedCost.value = cost
                    costSheetOpen.value = true
                }, onLongClick = {
                    viewModel.deleteCost(cost.cost)
                })
            }
        }
        FloatingActionButton(modifier=Modifier.testTag(CostTestTags.AddCostFloatingButton.tag),onClick = {
            selectedCost.value = null
            costSheetOpen.value = true
        }, containerColor = PrimaryVariant) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CostItem(
    index: Int,
    cost: Cost,
    category: CostCategory?,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryVariant
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = "هزینه ${index + 1}:", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = cost.amount.formatPrice(), color = Red)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "تومان", fontWeight = FontWeight.Bold)
                    }
                    Badge() {
                        Text(
                            text = category?.title ?: "حذف شده",
                            fontFamily = diroozFont,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = cost.description, fontSize = 13.sp)
            }

        }

    }
}
