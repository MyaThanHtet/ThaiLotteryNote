package com.mth.thailotterynote.ui.composables.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mth.thailotterynote.ui.composables.widget.PaidUnpaidView
import com.mth.thailotterynote.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = hiltViewModel()) {

    val lotteryDates by viewModel.getAllLotteryDates()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    var selectedDate by remember { mutableStateOf(lotteryDates.firstOrNull() ?: "") }

    LaunchedEffect(lotteryDates) {
        if (lotteryDates.isNotEmpty()) {
            selectedDate = lotteryDates.first()
        }
    }
    val dashboardData by viewModel.getDashboardData(selectedDate)
        .collectAsStateWithLifecycle(initialValue = null)

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Date Navigation
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

            dashboardData?.let { data ->
                // Display data using the DashboardData object
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                    ) {
                        PaidUnpaidView(data.paidCount, data.totalCount)
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${data.totalTicketsSold}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = "tickets sold",
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Total Amount (MMK)",
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${data.totalAmountMMK} KS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp
                        )
                        Text(
                            text = "${data.mmkTicketsSold} Tickets are sold with MMK"
                        )
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Total Amount (THB)",
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${data.totalAmountTHB} THB",
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp
                        )
                        Text(
                            text = "${data.thbTicketsSold} Tickets are sold with THB"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
            } ?: run {
                //CircularProgressIndicator()
                EmptyDataScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardHistoryPreview() {
    MaterialTheme {
        DashboardScreen()
    }
}
