package com.example.onlinemath

data class ExpressionResponse(
    val result: List<String>,
    val error: String? = null
)