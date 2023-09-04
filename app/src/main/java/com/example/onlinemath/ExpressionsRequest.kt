package com.example.onlinemath

data class ExpressionsRequest(
    val expr: List<String>,
    val precision: Int = 10
)