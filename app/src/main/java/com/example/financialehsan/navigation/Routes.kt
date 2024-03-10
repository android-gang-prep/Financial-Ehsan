package com.example.financialehsan.navigation

import com.example.financialehsan.R

enum class BottomBarRoutes(val route:String,val title:String,val icon:Int) {
    Costs(route = "costs",title = "هزینه ها",icon = R.drawable.ic_money),
    Revenues(route = "revenues",title = "درامد",icon = R.drawable.ic_card),
    BudgetManagement(route = "budget-management",title = "مدیریت بودجه",icon = R.drawable.ic_chart),
    Reminder(route = "reminder", title = "یادآور ها",icon = R.drawable.ic_clock)
}