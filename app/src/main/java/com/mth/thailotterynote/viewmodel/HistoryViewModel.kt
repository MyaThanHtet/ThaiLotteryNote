package com.mth.thailotterynote.viewmodel

import androidx.lifecycle.ViewModel
import com.mth.thailotterynote.model.User
import com.mth.thailotterynote.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    fun getAllLotteryDates(): Flow<List<String>> {
        return repository.getAllLotteryDates().map { date ->
            date.reversed()
        }
    }

    fun getAllUserByDate(date: String): Flow<List<User>> {
        return repository.getUsersByLotteryDate(date)
    }
}