package com.example.financialehsan.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.CostCategory
import com.example.financialehsan.database.entities.Revenue
import com.example.financialehsan.database.entities.RevenueCategory
import com.example.financialehsan.database.entities.relations.CostWithCategory
import com.example.financialehsan.database.entities.relations.RevenueWithCategory
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

class RevenueViewModel:ViewModel(),KoinComponent {
    private val revenueRepo:RevenueRepository by inject()
    private val categoryRepo:RevenueCategoryRepository by inject()

    private val _revenues = MutableStateFlow(emptyList<RevenueWithCategory>())
    val revenues = _revenues.asStateFlow()

    private val _categories = MutableStateFlow(emptyList<RevenueCategory>())
    val categories = _categories.asStateFlow()

    init {
        getRevenues()
        getCategories()
    }

    fun getRevenues(){
        viewModelScope.launch(Dispatchers.IO){
            revenueRepo.getRevenues().collect{result->
                _revenues.update { result }
            }
        }
    }
    fun deleteRevenue(revenue: Revenue){
        viewModelScope.launch(Dispatchers.IO){
            revenueRepo.deleteRevenue(revenue)
        }
    }

    fun getCategories(){
        viewModelScope.launch(Dispatchers.IO){
            categoryRepo.getCategories().collect{result->
                _categories.update { result }
            }
        }
    }

    fun addRevenue(revenue: Revenue){
        viewModelScope.launch(Dispatchers.IO) {
            revenueRepo.addRevenue(revenue)
        }
    }
    fun updateRevenue(revenue: Revenue){
        viewModelScope.launch(Dispatchers.IO) {
            revenueRepo.updateRevenue(revenue)
        }
    }

    fun addCategory(title:String){
        viewModelScope.launch(Dispatchers.IO){
            categoryRepo.addCategory(RevenueCategory(title = title))
        }
    }

    fun deleteCategory(category: RevenueCategory){
        viewModelScope.launch(Dispatchers.IO){
            categoryRepo.deleteCategory(category)
        }
    }
}