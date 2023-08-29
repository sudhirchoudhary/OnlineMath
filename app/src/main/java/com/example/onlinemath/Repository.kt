package com.example.onlinemath

import com.example.onlinemath.api.ApiService
import com.example.onlinemath.api.mapToResponse
import com.example.onlinemath.room.Expression
import com.example.onlinemath.room.ExpressionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService,
    private val expressionDao: ExpressionDao
) {
    suspend fun getResult(query: String) = apiService.getResult(query).mapToResponse()

    suspend fun insertExpressionInDb(expression: Expression) {
        withContext(Dispatchers.IO) {
            expressionDao.insertExpression(expression)
        }
    }

    fun getExpressionsFromDb(): Flow<List<Expression>> {
        return expressionDao.getAllExpressions()
    }

    suspend fun deleteAllExpressions() {
        withContext(Dispatchers.IO) {
            expressionDao.deleteAllExpressions()
        }
    }
}