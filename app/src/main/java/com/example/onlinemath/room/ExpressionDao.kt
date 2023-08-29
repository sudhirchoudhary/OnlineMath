package com.example.onlinemath.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpressionDao {

    @Query("SELECT * FROM expressions_table")
    fun getAllExpressions(): Flow<List<Expression>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpression(expression: Expression)

    @Query("DELETE FROM expressions_table")
    suspend fun deleteAllExpressions()
}