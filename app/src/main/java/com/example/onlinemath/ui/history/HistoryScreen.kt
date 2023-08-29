package com.example.onlinemath.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.onlinemath.room.Expression
import com.example.onlinemath.ui.main.HistoryScreenUiState
import com.example.onlinemath.ui.main.MainScreenUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    historyScreenUiState: HistoryScreenUiState,
    modifier: Modifier = Modifier,
    onEvent: (MainScreenUiEvent) -> Unit
) {

    LaunchedEffect(key1 = true) {
        onEvent(MainScreenUiEvent.OnFetchHistory)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onEvent(MainScreenUiEvent.OnBackEvent)}) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }

                IconButton(onClick = { onEvent(MainScreenUiEvent.OnDeleteEvent) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(historyScreenUiState.expressionsList) { expression ->
                HistoryItem(expression = expression)
            }
        }
    }
}

@Composable
fun HistoryItem(expression: Expression) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Text(text = expression.date)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "${expression.expression} -> ${expression.result}")
    }
}