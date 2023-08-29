package com.example.onlinemath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.onlinemath.ui.history.HistoryScreen
import com.example.onlinemath.ui.main.MainScreen
import com.example.onlinemath.ui.main.MainScreenUiEvent
import com.example.onlinemath.ui.main.MainViewModel
import com.example.onlinemath.ui.theme.OnlineMathTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentScreen by remember {
                mutableStateOf(0)
            }
            OnlineMathTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MainViewModel = hiltViewModel()
                    if(currentScreen == 0) {
                        val mainScreenUiState = viewModel.result.collectAsState()
                        MainScreen(mainScreenUiState.value) {
                            if(it == MainScreenUiEvent.OnHistoryEvent) {
                                currentScreen = 1
                                return@MainScreen
                            }
                            viewModel.onEvent(it)
                        }
                    } else if(currentScreen == 1) {
                        val historyScreenUiState = viewModel.historyList.collectAsState()
                        HistoryScreen(historyScreenUiState.value) {
                            if(it == MainScreenUiEvent.OnBackEvent) {
                                currentScreen = 0
                                return@HistoryScreen
                            }
                            viewModel.onEvent(it)
                        }
                    }
                }
            }
        }
    }
}