package com.example.onlinemath.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinemath.Repository
import com.example.onlinemath.api.Response
import com.example.onlinemath.convertMilliSecondsToDate
import com.example.onlinemath.room.Expression
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val TAG = "RequestX"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _result = MutableStateFlow(MainScreenUiState())
    private val expressionList = mutableListOf<Expression>()
    val result = _result.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        MainScreenUiState()
    )

    private val _historyList = MutableStateFlow(HistoryScreenUiState())

    val historyList = _historyList.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        HistoryScreenUiState()
    )

    private fun evaluateExpression(expr: String) {
        //val formattedExpression = expr.encodeToUrl()
        expressionList.clear()
        _result.value = MainScreenUiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val deferred = expr.mapToExpression().map {expression ->
                viewModelScope.async {
                    Log.d(TAG, "evaluateExpression: encoded exp = ${expression.encodedExpression}")
                    try {
                        repository.getResult(expression.encodedExpression).collect {
                            withContext(Dispatchers.Main) {
                                when (it) {
                                    is Response.Success -> {
                                        Log.d(TAG, "evaluateExpression: ${it.data}")
                                        expression.result = it.data
                                        expressionList.add(expression)
                                        repository.insertExpressionInDb(expression)
                                    }

                                    is Response.Error -> {
                                        _result.value = MainScreenUiState(
                                            isLoading = false,
                                            userMessage = it.error
                                        )
                                    }

                                    else -> {
                                        MainScreenUiState(isLoading = true)
                                    }
                                }
                            }
                        }
                    } catch (_: Exception) {

                    }
                }
            }

            deferred.awaitAll()
            _result.value = MainScreenUiState(expressionsList = expressionList, isLoading = false)
        }
    }

    fun getHistoryList() {
        viewModelScope.launch {
            _historyList.value = HistoryScreenUiState(
                isLoading = true
            )
            try {
                repository.getExpressionsFromDb().collect {
                    _historyList.value = HistoryScreenUiState(
                        isLoading = false,
                        expressionsList = it
                    )
                }
            } catch (_: Exception) {
                _historyList.value = HistoryScreenUiState(
                    isLoading = false
                )
            }
        }
    }

    private fun deleteAllHistory() {
        viewModelScope.launch {
            repository.deleteAllExpressions()
        }
    }

    fun onEvent(mainScreenUiEvent: MainScreenUiEvent) {
        when (mainScreenUiEvent) {
            is MainScreenUiEvent.OnSolveButtonClicked -> {
                evaluateExpression(mainScreenUiEvent.expr)
            }
            MainScreenUiEvent.OnDeleteEvent -> {
                deleteAllHistory()
            }
            MainScreenUiEvent.OnFetchHistory -> {
                getHistoryList()
            }
            else -> {}
        }
    }
}

data class MainScreenUiState(
    val isLoading: Boolean = false,
    val expressionsList: List<Expression> = emptyList(),
    val userMessage: String = ""
)

data class HistoryScreenUiState(
    val isLoading: Boolean = false,
    val expressionsList: List<Expression> = emptyList()
)

sealed interface MainScreenUiEvent {
    data class OnSolveButtonClicked(val expr: String) : MainScreenUiEvent
    object OnBackEvent: MainScreenUiEvent
    object OnDeleteEvent: MainScreenUiEvent
    object OnHistoryEvent: MainScreenUiEvent
    object OnFetchHistory: MainScreenUiEvent
}

fun String.mapToExpression(): List<Expression> {
    val individualExpressions = split("\n")
    return individualExpressions.map {
        Expression(
            expression = it,
            encodedExpression = it,
            date = System.currentTimeMillis().convertMilliSecondsToDate()
        )
    }
}