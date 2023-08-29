package com.example.onlinemath.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expressions_table")
data class Expression(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val expression: String = "",
    val encodedExpression: String = "",
    val date: String = "",
    var result: String = ""
)
