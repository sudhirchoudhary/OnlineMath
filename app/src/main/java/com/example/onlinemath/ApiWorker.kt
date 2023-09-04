package com.example.onlinemath

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.onlinemath.api.Response
import com.example.onlinemath.room.Expression
import com.example.onlinemath.ui.main.TAG
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltWorker
class ApiWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: Repository
) : CoroutineWorker(applicationContext, workerParameters) {

    private val expressions = inputData.getStringArray("expressions") ?: emptyArray()

    override suspend fun doWork(): Result {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getResultPost(
                ExpressionsRequest(
                    expr = expressions.toList()
                )
            ).collect {
                withContext(Dispatchers.Main) {
                    when (it) {
                        is Response.Success -> {
                            it.data.result.forEachIndexed { index, s ->
                                val expression = Expression(
                                    expression = expressions[index],
                                    encodedExpression = expressions[index],
                                    date = System.currentTimeMillis().convertMilliSecondsToDate(),
                                    result = s
                                )
                                repository.insertExpressionInDb(expression)
                            }
                        }

                        is Response.Error -> {

                        }

                        else -> {
                        }
                    }
                }
            }
        }
        return Result.success()
    }


}

