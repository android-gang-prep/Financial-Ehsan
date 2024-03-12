package com.example.financialehsan.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financialehsan.repositories.CostRepository
import com.example.financialehsan.repositories.RevenueRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class MainViewModel(
    private val costRepo: CostRepository,
    private val revenueRepo: RevenueRepository
) : ViewModel() {

    private val _sumOfCosts = MutableStateFlow(0)
    val sumOfCosts = _sumOfCosts.asStateFlow()

    private val _sumOfRevenues = MutableStateFlow(0)
    val sumOfRevenues = _sumOfRevenues.asStateFlow()

    init {
        getSums()
    }

    fun getSums() {
        getSumOfCosts()
        getSumOfRevenues()
    }

    private fun getSumOfCosts() {
        viewModelScope.launch(Dispatchers.IO) {
            costRepo.getCosts().collect { result ->
                _sumOfCosts.update { result.sumOf { it.cost.amount }.toInt() }
            }
        }
    }

    private fun getSumOfRevenues() {
        viewModelScope.launch(Dispatchers.IO) {
            revenueRepo.getRevenues().collect { result ->
                _sumOfRevenues.update { result.sumOf { it.revenue.amount }.toInt() }
            }
        }
    }
}