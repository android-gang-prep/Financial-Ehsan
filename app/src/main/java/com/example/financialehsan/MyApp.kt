package com.example.financialehsan

import android.app.Application
import androidx.room.Room
import com.example.financialehsan.database.AppDatabase
import com.example.financialehsan.database.entities.CostCategory
import com.example.financialehsan.database.entities.RevenueCategory
import com.example.financialehsan.repositories.BudgetRepository
import com.example.financialehsan.repositories.CostCategoryRepository
import com.example.financialehsan.repositories.CostRepository
import com.example.financialehsan.repositories.ReminderRepository
import com.example.financialehsan.repositories.RevenueCategoryRepository
import com.example.financialehsan.repositories.RevenueRepository
import com.example.financialehsan.viewModels.BudgetViewModel
import com.example.financialehsan.viewModels.CostViewModel
import com.example.financialehsan.viewModels.MainViewModel
import com.example.financialehsan.viewModels.ReminderViewModel
import com.example.financialehsan.viewModels.RevenueViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module


val defaultCostCategories = listOf(
    "غذا","سرگرمی","ورزش"
)
val defaultRevenueCategories = listOf(
    "فریلنسری","حقوق ماهیانه"
)

class MyApp: Application() {

    private lateinit var koinApp:KoinApplication
    private val databaseScope = CoroutineScope(SupervisorJob()+Dispatchers.IO)

    companion object{
        val daoModule = module {
            single {
                val room:AppDatabase = get()
                room.costCategoryDao()
            }
            single {
                val room:AppDatabase = get()
                room.revenueCategoryDao()
            }
            single {
                val room:AppDatabase = get()
                room.costDao()
            }
            single {
                val room:AppDatabase = get()
                room.revenueDao()
            }
            single {
                val room:AppDatabase = get()
                room.budgetDao()
            }
            single {
                val room:AppDatabase = get()
                room.reminderDao()
            }
        }
        val repositoryModule = module {
            single {
                CostCategoryRepository(get())
            }
            single {
                RevenueCategoryRepository(get())
            }
            single {
                CostRepository(get())
            }
            single {
                RevenueRepository(get())
            }
            single {
                BudgetRepository(get())
            }
            single {
                ReminderRepository(get())
            }
        }
        val viewModelModule = module {
            viewModel {
                BudgetViewModel(get(),get())
            }
            viewModel{
                CostViewModel(get(),get(),get())
            }
            viewModel{
                ReminderViewModel(get(),get(),get())
            }
            viewModel {
                RevenueViewModel(get(),get())
            }
            viewModel {
                MainViewModel(get(),get())
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        val context = this.applicationContext

        koinApp = startKoin {
            modules(
                module {
                    single {
                        Room.databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            "app_db"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                },
                daoModule,
                repositoryModule,
                viewModelModule
            )
        }

        insertDefaultCategories()
    }

    private fun insertDefaultCategories(){
        val costCategoryRepo:CostCategoryRepository = koinApp.koin.get()
        val revenueCategoryRepo:RevenueCategoryRepository = koinApp.koin.get()
        databaseScope.launch {
            val costCategories = costCategoryRepo.getLatestCategories()
            val revenueCategories = revenueCategoryRepo.getLatestCategories()

            if (!costCategories.map { it.title }.containsAll(defaultCostCategories)){
                costCategoryRepo.addCategories(defaultCostCategories.map { CostCategory(title = it) })
            }
            if (!revenueCategories.map { it.title }.containsAll(defaultRevenueCategories)){
                revenueCategoryRepo.addCategories(defaultRevenueCategories.map { RevenueCategory(title = it) })
            }
        }
    }
}

