package com.example.financialehsan.viewModels

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.financialehsan.database.AppDatabase
import com.example.financialehsan.database.dao.CostCategoryDao
import com.example.financialehsan.database.dao.CostDao
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.CostCategory
import com.example.financialehsan.database.entities.relations.CostWithCategory
import com.example.financialehsan.repositories.BudgetRepository
import com.example.financialehsan.repositories.CostCategoryRepository
import com.example.financialehsan.repositories.CostRepository
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin


@RunWith(AndroidJUnit4::class)
class CostTest : TestCase() {

    private lateinit var room: AppDatabase
    private lateinit var costCategoryDao: CostCategoryDao
    private lateinit var costDao: CostDao

    lateinit var viewModel: CostViewModel

    @Before
    fun start() {

        room = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        costDao = room.costDao()
        costCategoryDao = room.costCategoryDao()

        val costRepo = CostRepository(costDao)
        val budgetRepo = BudgetRepository(room.budgetDao())
        val costCategoryRepo = CostCategoryRepository(room.costCategoryDao())

        viewModel = CostViewModel(costRepo, budgetRepo, costCategoryRepo)
    }

    @After
    fun close() {
        room.close()
        stopKoin()
    }


    @Test
    fun `test cost adding correctly`() = runTest {
        val category = "x"
        costCategoryDao.addCategory(CostCategory(title = category))
        val latestCategories = costCategoryDao.getLatestCategories()
        assertNotNull(latestCategories.find { it.title == category })
        val costAmount = 50000L
        val costDescription = "Testing cost"
        costDao.addCost(
            Cost(
                categoryId = latestCategories.find { it.title == "x" }!!.id,
                description = costDescription,
                amount = costAmount
            )
        )
        val latestCosts = costDao.getLatestCosts()
        assert(latestCosts.isNotEmpty())
        assert(latestCosts.any { it.cost.description == costDescription && it.cost.amount == costAmount && it.category?.title == category })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test() = runTest(StandardTestDispatcher()) {
        viewModel.addCategory("test")
        viewModel.getLatestCategories().also { println("categories: $it") }
    }

}