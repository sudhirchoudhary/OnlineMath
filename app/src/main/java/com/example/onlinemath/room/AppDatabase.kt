package com.example.onlinemath.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Expression::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expressionDao(): ExpressionDao
}