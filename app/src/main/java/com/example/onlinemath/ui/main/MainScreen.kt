package com.example.onlinemath.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainScreenUiState: MainScreenUiState,
    modifier: Modifier = Modifier,
    onEvent: (MainScreenUiEvent) -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }

    val animatedHeight = animateDpAsState(
        targetValue = if(text.isNotEmpty()) 400.dp else 260.dp,
        label = "",
        animationSpec = tween(1000, easing = LinearEasing)
    )
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            placeholder = { Text(text = "Enter your equations") },
            modifier = Modifier.fillMaxSize(),
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AnimatedVisibility(visible = mainScreenUiState.isLoading) {
                CircularProgressIndicator()
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = mainScreenUiState.expressionsList.joinToString("\n", transform = {
                    "${it.expression} -> ${it.result}"
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        shape = RectangleShape,
                        color = MaterialTheme.colorScheme.primary
                    )
                    .height(animatedHeight.value)
                    .defaultMinSize(minHeight = 260.dp)
                    .padding(16.dp)
            )
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {

                CircularButton(text = "Solve", enabled = !mainScreenUiState.isLoading) {
                    onEvent(MainScreenUiEvent.OnSolveButtonClicked(text))
                }

                IconButton(onClick = { onEvent(MainScreenUiEvent.OnHistoryEvent) }, modifier = Modifier.size(60.dp)) {
                    Icon(imageVector = Icons.Outlined.History, contentDescription = null, modifier = Modifier.size(40.dp))
                }
            }
        }
    }
}

@Composable
fun CircularButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 80.dp,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier.size(size),
        onClick = { onClick() },
        enabled = enabled,
        shape = CircleShape
    ) {
        Text(text = text)
    }
}