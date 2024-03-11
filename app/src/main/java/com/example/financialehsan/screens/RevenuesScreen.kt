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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financialehsan.LocalAppState
import com.example.financialehsan.screens.components.AppTextField
import com.example.financialehsan.screens.components.BorderButton
import com.example.financialehsan.screens.components.SelectableButton
import com.example.financialehsan.ui.theme.PrimaryVariant
import com.example.financialehsan.ui.theme.Red
import com.example.financialehsan.ui.theme.diroozFont
import com.example.financialehsan.utils.formatPrice
import com.example.financialehsan.viewModels.RevenueViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financialehsan.database.entities.Revenue
import com.example.financialehsan.database.entities.RevenueCategory
import com.example.financialehsan.database.entities.relations.RevenueWithCategory
import com.example.financialehsan.defaultRevenueCategories
import com.example.financialehsan.ui.theme.Green

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevenuesScreen(viewModel: RevenueViewModel = viewModel()) {
    val appState = LocalAppState.current
    val revenues by viewModel.revenues.collectAsState()
    val categories by viewModel.categories.collectAsState()

    val revenueSheetOpen = remember {
        mutableStateOf(false)
    }
    val selectedRevenue = remember {
        mutableStateOf<RevenueWithCategory?>(null)
    }
    val newCategorySheetOpen = remember {
        mutableStateOf(false)
    }
    val revenueSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val categorySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (revenueSheetOpen.value) {
        val editMode = selectedRevenue.value != null
        val revenueAmount = remember {
            mutableStateOf(selectedRevenue.value?.revenue?.amount?.toString().orEmpty())
        }
        val revenueDescription = remember {
            mutableStateOf(selectedRevenue.value?.revenue?.description.orEmpty())
        }
        val selectedCategory = remember {
            mutableIntStateOf(selectedRevenue.value?.category?.id ?: (categories.firstOrNull()?.id ?: 0))
        }
        ModalBottomSheet(
            onDismissRequest = {
                revenueSheetOpen.value = false
            },
            sheetState = revenueSheetState,
            containerColor = PrimaryVariant,
            shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Column(
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp, start = 16.dp)
                        .navigationBarsPadding()
                ) {
                    Text(text = if (!editMode) "اضافه کردن درامد" else "ویرایش درامد", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(
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
                                    if (it.title !in defaultRevenueCategories && it.id != selectedCategory.intValue){
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
                        value = revenueAmount.value,
                        onValueChange = {
                            revenueAmount.value = it
                        },
                        placeholder = "مقدار درامد (تومان)",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AppTextField(
                        modifier = Modifier.fillMaxWidth(),
                        height = 200.dp,
                        value = revenueDescription.value,
                        onValueChange = {
                            revenueDescription.value = it
                        },
                        placeholder = "توضیحات درامد"
                    )
                    Spacer(modifier = Modifier.heightIn(22.dp))
                    Button(onClick = {
                        if (revenueDescription.value.isNotBlank() && revenueAmount.value.toLongOrNull() != null) {
                            if (editMode){
                                println(selectedCategory.intValue)
                                viewModel.updateRevenue(selectedRevenue.value!!.revenue.copy(
                                    categoryId = selectedCategory.intValue,
                                    description = revenueDescription.value,
                                    amount = revenueAmount.value.toLong()
                                ))
                            }else{
                                viewModel.addRevenue(
                                    Revenue(
                                        categoryId = selectedCategory.intValue,
                                        description = revenueDescription.value,
                                        amount = revenueAmount.value.toLong()
                                    )
                                )
                            }
                            appState.scope.launch {
                                revenueSheetState.hide()
                            }
                                .invokeOnCompletion {
                                    revenueSheetOpen.value = false
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
            itemsIndexed(revenues) { index, revenue ->
                RevenueItem(index = index, revenue = revenue.revenue, category = revenue.category, onClick = {
                    selectedRevenue.value = revenue
                    revenueSheetOpen.value = true
                }, onLongClick = {
                    viewModel.deleteRevenue(revenue.revenue)
                })
            }
        }
        FloatingActionButton(onClick = {
            selectedRevenue.value = null
            revenueSheetOpen.value = true
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
private fun RevenueItem(index: Int, revenue: Revenue, category: RevenueCategory?, onClick:()->Unit, onLongClick:()->Unit) {
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = "درامد ${index + 1}:", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = revenue.amount.formatPrice(), color = Green)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "تومان", fontWeight = FontWeight.Bold)
                    }
                    

                    Badge(
                        containerColor = Green,
                        contentColor = contentColorFor(backgroundColor = Green)
                    ) {
                        Text(
                            text = category?.title ?: "حذف شده",
                            fontFamily = diroozFont,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = revenue.description, fontSize = 13.sp)
            }

        }

    }
}