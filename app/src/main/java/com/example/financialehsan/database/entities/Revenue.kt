package com.example.financialehsan.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("revenue")
data class Revenue(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("revenue_id")
    val id:Int,
    val amount:Double,
    val categoryId:Int
)
