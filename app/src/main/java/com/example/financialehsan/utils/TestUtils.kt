package com.example.financialehsan.utils

fun areWeInTestMode():Boolean{
    Thread.currentThread().stackTrace.forEach {
        if (it.className.startsWith("org.junit.")){
            return true
        }
    }
    return false
}