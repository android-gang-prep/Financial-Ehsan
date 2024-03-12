package com.example.financialehsan.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.CostCategory
import com.example.financialehsan.database.entities.Budget
import com.example.financialehsan.database.entities.relations.BudgetWithCategory
import com.example.financialehsan.repositories.BudgetRepository
import com.example.financialehsan.repositories.CostCategoryRepository
import com.example.financialehsan.repositories.CostRepository
import com.example.financialehsan.repositories.RevenueCategoryRepository
import com.example.financialehsan.repositories.RevenueRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BudgetViewModel(
    private val budgetRepo:BudgetRepository,
    private val categoryRepo:CostCategoryRepository
):ViewModel() {

    private val _budgets = MutableStateFlow(emptyList<BudgetWithCategory>())
    val budgets = _budgets.asStateFlow()

    private val _categories = MutableStateFlow(emptyList<CostCategory>())
    val categories = _categories.asStateFlow()

    init {
        getBudgets()
        getCategories()
    }

    fun getBudgets(){
        viewModelScope.launch(Dispatchers.IO){
            budgetRepo.getBudgets().collect{result->
                _budgets.update { result }
            }
        }
    }
    fun deleteBudget(budget: Budget){
        viewModelScope.launch(Dispatchers.IO){
            budgetRepo.deleteBudget(budget)
        }
    }

    fun getCategories(){
        viewModelScope.launch(Dispatchers.IO){
            categoryRepo.getCategories().collect{result->
                _categories.update { result }
            }
        }
    }

    fun addBudget(budget: Budget){
        viewModelScope.launch(Dispatchers.IO) {
            budgetRepo.addBudget(budget)
        }
    }
    fun updateBudget(budget: Budget){
        viewModelScope.launch(Dispatchers.IO) {
            budgetRepo.updateBudget(budget)
        }
    }

    fun addCategory(title:String){
        viewModelScope.launch(Dispatchers.IO){
            categoryRepo.addCategory(CostCategory(title = title))
        }
    }

    fun deleteCategory(category: CostCategory){
        viewModelScope.launch(Dispatchers.IO){
            categoryRepo.deleteCategory(category)
        }
    }
}