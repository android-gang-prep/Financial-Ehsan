package com.example.financialehsan.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financialehsan.LocalAppState
import com.example.financialehsan.defaultRevenueCategories
import com.example.financialehsan.screens.components.AppTextField
import com.example.financialehsan.screens.components.BorderButton
import com.example.financialehsan.screens.components.SelectableButton
import com.example.financialehsan.ui.theme.Green
import com.example.financialehsan.ui.theme.PrimaryVariant
import com.example.financialehsan.utils.formatPrice
import com.example.financialehsan.viewModels.BudgetViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financialehsan.database.entities.Budget
import com.example.financialehsan.database.entities.CostCategory
import com.example.financialehsan.database.entities.relations.BudgetWithCategory
import com.example.financialehsan.defaultCostCategories

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetManagementScreen(viewModel:BudgetViewModel = viewModel()) {
    val appState = LocalAppState.current
    val budgets by viewModel.budgets.collectAsState()
    val categories by viewModel.categories.collectAsState()

    val budgetSheetOpen = remember {
        mutableStateOf(false)
    }
    val selectedBudget = remember {
        mutableStateOf<BudgetWithCategory?>(null)
    }
    val newCategorySheetOpen = remember {
        mutableStateOf(false)
    }
    val budgetSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val categorySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (budgetSheetOpen.value) {
        val filteredCategories = categories.filter { it.id !in budgets.map { it.category.id } }
        val editMode = selectedBudget.value != null
        val budgetAmount = remember {
            mutableStateOf(selectedBudget.value?.budget?.amount?.toString().orEmpty())
        }
        val selectedCategory = remember {
            mutableStateOf(selectedBudget.value?.category?.id ?: (filteredCategories.firstOrNull()?.id))
        }
        ModalBottomSheet(
            onDismissRequest = {
                budgetSheetOpen.value = false
            },
            sheetState = budgetSheetState,
            containerColor = PrimaryVariant,
            shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Column(
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp, start = 16.dp)
                        .navigationBarsPadding()
                ) {
                    Text(text = if (!editMode) "اضافه کردن بودجه" else "ویرایش بودجه", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(filteredCategories) {
                            SelectableButton(
                                text = it.title,
                                selected = it.id == selectedCategory.value,
                                onSelect = {
                                    selectedCategory.value = it.id
                                },
                                onLongClick = {
                                    if (it.title !in defaultCostCategories && it.id != selectedCategory.value){
                                        viewModel.deleteCategory(it)
                                    }
                                }
                            )
                        }
                        item {
                            BorderButton(onClick = {
                                newCategorySheetOpen.value = true
                            }) {
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
                        value = budgetAmount.value,
                        onValueChange = {
                            budgetAmount.value = it
                        },
                        placeholder = "مقدار بودجه (تومان)",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.heightIn(22.dp))
                    Button(onClick = {
                        if (budgetAmount.value.toLongOrNull() != null && selectedCategory.value != null) {
                            if (editMode){
                                viewModel.updateBudget(selectedBudget.value!!.budget.copy(
                                    categoryId = selectedCategory.value!!,
                                    amount = budgetAmount.value.toLong()
                                ))
                            }else{
                                viewModel.addBudget(
                                    Budget(
                                        categoryId = selectedCategory.value!!,
                                        amount = budgetAmount.value.toLong()
                                    )
                                )
                            }
                            appState.scope.launch {
                                budgetSheetState.hide()
                            }
                                .invokeOnCompletion {
                                    budgetSheetOpen.value = false
                                }
                        }

                    }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
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
                        placeholder = "عنوان دسته بندی"
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
                    }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                        Text(text = "اضافه کردن")
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
            items(budgets) { budget ->
                BudgetItem(budget = budget.budget, category = budget.category, onClick = {
                    selectedBudget.value = budget
                    budgetSheetOpen.value = true
                }, onLongClick = {
                    viewModel.deleteBudget(budget.budget)
                })
            }
        }
        FloatingActionButton(onClick = {
            selectedBudget.value = null
            budgetSheetOpen.value = true
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
private fun BudgetItem(budget: Budget, category: CostCategory?, onClick:()->Unit, onLongClick:()->Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize(), colors = CardDefaults.cardColors(
            containerColor = PrimaryVariant
        ),
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )){
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "بودجه برای ${category?.title}", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = budget.amount.formatPrice(), color = Green)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "تومان", fontWeight = FontWeight.Bold)
                }
            }

        }

    }
}