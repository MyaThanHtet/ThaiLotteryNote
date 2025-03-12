package com.mth.thailotterynote.ui.composables.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mth.thailotterynote.R
import com.mth.thailotterynote.model.LottoResponse
import com.mth.thailotterynote.model.WinningUserWithPrize
import com.mth.thailotterynote.network.NetworkResult
import com.mth.thailotterynote.ui.uitls.convertThaiDateToEnglish
import com.mth.thailotterynote.viewmodel.WinnersViewModel

@Composable
fun WinnersScreen(viewModel: WinnersViewModel = hiltViewModel()) {
    val lotteryResult by viewModel.lotteryResult.collectAsState()
    val winningUsersWithPrize by viewModel.winningUsersWithPrize.collectAsState()


    LaunchedEffect(key1 = Unit) {
        viewModel.fetchLotteryResults()
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val lotteDate = lotteryResult.data?.response?.date


            Text(
                text = "-- Winner List --",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6A3EA1),
                modifier = Modifier

                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = if (lotteDate != null) convertThaiDateToEnglish(lotteDate) else "",
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            when (lotteryResult) {
                is NetworkResult.Success -> {
                    Column {
                        LazyColumn {
                            items(winningUsersWithPrize) { winningUser ->
                                WinnerCard(winningUser)
                            }
                        }

                        if (winningUsersWithPrize.isEmpty()) {
                            Text(
                                "No winning tickets found.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                is NetworkResult.Error -> {
                    val errorMessage = (lotteryResult as NetworkResult.Error<LottoResponse>).message
                    Text("Error: $errorMessage")
                }

                is NetworkResult.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }

}

@Composable
fun WinnerCard(winner: WinningUserWithPrize) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = winner.user.name.uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formatLotteryNumber(winner.ticketNumber),
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = winner.prizeName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    )

                    Text(
                        text = "${winner.reward} THB",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )


                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.End,

                    ) {
                    Image(
                        painter = painterResource(id = R.drawable.winner_badge),
                        contentDescription = "winner_badge",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(8.dp)

                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    Text(
                        text = winner.date,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(8.dp)
                    )
                }


            }
        }


    }
}

fun formatLotteryNumber(number: String): String {
    return number.chunked(1).joinToString(" ")
}



