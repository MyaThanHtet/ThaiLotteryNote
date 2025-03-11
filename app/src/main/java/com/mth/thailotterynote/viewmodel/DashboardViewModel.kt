package com.mth.thailotterynote.viewmodel

import androidx.lifecycle.ViewModel
import com.mth.thailotterynote.model.DashboardData
import com.mth.thailotterynote.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    fun getAllLotteryDates(): Flow<List<String>> {
        return repository.getAllLotteryDates().map { date ->
            date.reversed()
        }
    }


    fun getDashboardData(date: String): Flow<DashboardData?> {
        return repository.getDashboardHistoryForDate(date)
            .map { history ->
                history?.let {
                    DashboardData(
                        paidCount = it.numberOfPaid,
                        totalCount = history.totalSoldCount,
                        totalTicketsSold = history.totalSoldCount,
                        totalAmountMMK = history.totalPriceMMK,
                        mmkTicketsSold = history.numTicketMMK,
                        totalAmountTHB = history.totalPriceTHB,
                        thbTicketsSold = history.numTicketTHB
                    )
                }
            }
    }
}