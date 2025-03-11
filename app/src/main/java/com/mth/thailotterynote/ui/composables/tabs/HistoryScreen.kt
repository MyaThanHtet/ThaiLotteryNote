package com.mth.thailotterynote.ui.composables.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mth.thailotterynote.ui.composables.UserHistoryItem
import com.mth.thailotterynote.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val lotteryDates by viewModel.getAllLotteryDates()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    var selectedDate by remember { mutableStateOf(lotteryDates.firstOrNull() ?: "") }
    val historyData by viewModel.getAllUserByDate(selectedDate)
        .collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(lotteryDates) {
        if (lotteryDates.isNotEmpty()) {
            selectedDate = lotteryDates.first()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    val currentIndex = lotteryDates.indexOf(selectedDate)
                    if (currentIndex < lotteryDates.size - 1) {
                        selectedDate = lotteryDates[currentIndex + 1]
                    }

                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Date")
                }
                Text(
                    text = selectedDate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                IconButton(onClick = {
                    val currentIndex = lotteryDates.indexOf(selectedDate)
                    if (currentIndex > 0) {
                        selectedDate = lotteryDates[currentIndex - 1]
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Date")
                }
            }

            historyData.let { data ->
                if (data != null) {
                    if (data.isEmpty()) {
                        EmptyDataScreen()
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(bottom = 100.dp)
                        ) {
                            items(data) { data ->
                                UserHistoryItem(data)
                            }
                        }

                    }
                }
            }


        }
    }
}