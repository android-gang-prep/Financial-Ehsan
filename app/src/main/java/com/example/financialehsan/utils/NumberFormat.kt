package com.example.financialehsan.utils

import java.text.DecimalFormat

fun Long.formatPrice():String{
    val formatter = DecimalFormat("#,###")
    return formatter.format(this)
}