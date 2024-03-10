package com.example.financialehsan.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("costs")
data class Cost(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("cost_id")
    val id:Int = 0,
    @ColumnInfo("category_id")
    val categoryId:Int,
    val description:String,
    val amount:Long,
    @ColumnInfo("created_at",)
    val createdAt:Long = System.currentTimeMillis()
)
