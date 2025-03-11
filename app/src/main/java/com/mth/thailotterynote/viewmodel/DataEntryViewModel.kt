package com.mth.thailotterynote.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mth.thailotterynote.model.User
import com.mth.thailotterynote.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DataEntryViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    fun saveUserData(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }
}