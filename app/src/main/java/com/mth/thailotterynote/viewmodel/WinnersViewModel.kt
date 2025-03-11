package com.mth.thailotterynote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mth.thailotterynote.model.LottoResponse
import com.mth.thailotterynote.model.WinningUserWithPrize
import com.mth.thailotterynote.network.NetworkResult
import com.mth.thailotterynote.repository.UserRepository
import com.mth.thailotterynote.ui.uitls.convertThaiDateToEnglish
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WinnersViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _lotteryResult =
        MutableStateFlow<NetworkResult<LottoResponse>>(NetworkResult.Loading())
    val lotteryResult: StateFlow<NetworkResult<LottoResponse>> = _lotteryResult.asStateFlow()

    private val _winningUsersWithPrize = MutableStateFlow<List<WinningUserWithPrize>>(emptyList())
    val winningUsersWithPrize: StateFlow<List<WinningUserWithPrize>> =
        _winningUsersWithPrize.asStateFlow()


    fun fetchLotteryResults() {
        viewModelScope.launch {
            _lotteryResult.value = NetworkResult.Loading()
            val result = repository.fetchData()
            _lotteryResult.value = result

            if (result is NetworkResult.Success) {
                checkWinningTickets(result.data)
            }
        }
    }

    private fun translatePrizeName(thaiPrizeName: String): String {
        return when (thaiPrizeName) {
            "prizeFirst" -> "First Prize"
            "prizeFirstNear" -> "First Near Prize"
            "prizeSecond" -> "Second Prize"
            "prizeThird" -> "Third Prize"
            "prizeForth" -> "Fourth Prize"
            "prizeFifth" -> "Fifth Prize"
            "runningNumberFrontThree" -> "Front 3 Digits"
            "runningNumberBackThree" -> "Back 3 Digits"
            "runningNumberBackTwo" -> "Back 2 Digits"
            else -> "Unknown Prize"
        }
    }

    private suspend fun checkWinningTickets(lottoResponse: LottoResponse?) {
        if (lottoResponse == null) return
        val resultDate = convertThaiDateToEnglish(lottoResponse.response.date)

        val winningNumbersMap = lottoResponse.response.prizes
            .flatMap { prize -> prize.number.map { it to (prize.id to prize.reward) } }
            .toMap()

        val allUsers = repository.getAllUsers().first()
        val winningUsers = allUsers.filter { user ->
            val ticketNumber = user.ticketNumber
            user.lotteryDate == resultDate.uppercase() &&
                    (ticketNumber in winningNumbersMap || getRunningNumberPrizeAndReward(
                        ticketNumber,
                        lottoResponse
                    ).first.isNotEmpty())
        }.map { user ->

            val (thaiPrizeName, mainReward) = winningNumbersMap[user.ticketNumber] ?: ("" to "0")
            val mainPrizeName = translatePrizeName(thaiPrizeName)

            val (runningNumberPrizeName, runningReward) = getRunningNumberPrizeAndReward(
                user.ticketNumber,
                lottoResponse
            )


            val finalPrizeName = listOf(mainPrizeName, runningNumberPrizeName)
                .filter { it.isNotEmpty() && it != "Unknown Prize" }
                .joinToString(", ")
                .ifEmpty { runningNumberPrizeName }


            val finalReward = listOf(mainReward, runningReward)
                .filter { it.isNotEmpty() && it != "0" }
                .joinToString(", ")

            WinningUserWithPrize(user, finalPrizeName, user.ticketNumber, finalReward,resultDate)
        }

        _winningUsersWithPrize.value = winningUsers
    }


    private fun getRunningNumberPrizeAndReward(
        ticketNumber: String,
        lottoResponse: LottoResponse
    ): Pair<String, String> {
        var prizeName = ""
        var reward = "0"

        lottoResponse.response.runningNumbers.forEach { runningNumber ->
            if (
                (runningNumber.id == "runningNumberFrontThree" && runningNumber.number.contains(
                    ticketNumber.take(3)
                )) ||
                (runningNumber.id == "runningNumberBackThree" && runningNumber.number.contains(
                    ticketNumber.takeLast(3)
                )) ||
                (runningNumber.id == "runningNumberBackTwo" && runningNumber.number.contains(
                    ticketNumber.takeLast(2)
                ))
            ) {
                val runningPrizeName = when (runningNumber.id) {
                    "runningNumberFrontThree" -> "Running Number Front Three"
                    "runningNumberBackThree" -> "Running Number Back Three"
                    "runningNumberBackTwo" -> "Running Number Back Two"
                    else -> ""
                }

                if (prizeName.isNotEmpty()) {
                    prizeName += ", "
                    reward += ", "
                }

                prizeName += runningPrizeName
                reward = runningNumber.reward
            }
        }

        return prizeName to reward
    }

}