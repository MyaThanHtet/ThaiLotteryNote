package com.mth.thailotterynote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mth.thailotterynote.database.dao.DashboardHistoryDao
import com.mth.thailotterynote.database.dao.UserDao
import com.mth.thailotterynote.model.DashboardHistory
import com.mth.thailotterynote.model.LottoResponse
import com.mth.thailotterynote.model.User
import com.mth.thailotterynote.network.ApiService
import com.mth.thailotterynote.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val dashboardHistoryDao: DashboardHistoryDao,
    private val apiService: ApiService
) {

    suspend fun insertUser(user: User) = withContext(Dispatchers.IO) {
        userDao.insert(user)
        updateDashboardHistory(user, isInsert = true)
    }

    suspend fun updateUser(user: User) = withContext(Dispatchers.IO) {
        val oldUser = userDao.getUserById(user.id)
        userDao.update(user)
        updateDashboardHistoryOnUserUpdate(oldUser, user)
    }

    suspend fun deleteUser(user: User) = withContext(Dispatchers.IO) {
        userDao.delete(user)
        updateDashboardHistory(user, isInsert = false)
        cleanupDashboardHistoryIfEmpty(user.lotteryDate)
    }

    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
    fun getAllLotteryDates(): Flow<List<String>> = userDao.getAllLotteryDates()
    fun getDashboardHistoryForDate(date: String): Flow<DashboardHistory?> = dashboardHistoryDao.getDashboardHistoryForDate(date)
    fun getUsersByLotteryDate(date: String): Flow<List<User>> = userDao.getUsersByLotteryDate(date)

    private suspend fun updateDashboardHistory(user: User, isInsert: Boolean) = withContext(Dispatchers.IO) {
        val existingHistory = dashboardHistoryDao.getDashboardHistoryForDate(user.lotteryDate).firstOrNull()
        val change = if (isInsert) 1 else -1

        val updatedHistory = existingHistory?.copy(
            totalSoldCount = existingHistory.totalSoldCount + change,
            numberOfPaid = existingHistory.numberOfPaid + (if (user.isPaid) change else 0),
            numberOfUnpaid = existingHistory.numberOfUnpaid + (if (!user.isPaid) change else 0),
            totalPriceMMK = existingHistory.totalPriceMMK + if (user.currency.contains("MMK")) user.price.toDouble() * change else 0.0,
            totalPriceTHB = existingHistory.totalPriceTHB + if (user.currency.contains("THB")) user.price.toDouble() * change else 0.0,

            numTicketMMK = existingHistory.numTicketMMK + (if (user.currency.contains("MMK")) change else 0),
            numTicketTHB = existingHistory.numTicketTHB + (if (user.currency.contains("THB")) change else 0)
        )

        if (existingHistory != null) {
            dashboardHistoryDao.update(updatedHistory!!)
        } else if (isInsert) {
            val newHistory = DashboardHistory(
                dueDate = user.lotteryDate,
                totalSoldCount = 1,
                numberOfPaid = if (user.isPaid) 1 else 0,
                numberOfUnpaid = if (!user.isPaid) 1 else 0,
                totalPriceMMK = if (user.currency.contains("MMK")) user.price.toDouble() else 0.0,
                totalPriceTHB = if (user.currency.contains("THB")) user.price.toDouble() else 0.0,
                numTicketMMK = if (user.currency.contains("MMK")) 1 else 0,
                numTicketTHB = if (user.currency.contains("THB")) 1 else 0
            )
            dashboardHistoryDao.insert(newHistory)
        }
    }


    private suspend fun cleanupDashboardHistoryIfEmpty(date: String) = withContext(Dispatchers.IO) {
        val existingHistory = dashboardHistoryDao.getDashboardHistoryForDate(date).firstOrNull()
        if (existingHistory != null && existingHistory.totalSoldCount == 0) {
            dashboardHistoryDao.delete(existingHistory)
        }
    }

    private suspend fun updateDashboardHistoryOnUserUpdate(oldUser: User?, newUser: User) = withContext(Dispatchers.IO) {
        if (oldUser == null) return@withContext
        updateDashboardHistory(oldUser, isInsert = false)
        updateDashboardHistory(newUser, isInsert = true)
    }

    suspend fun fetchData(): NetworkResult<LottoResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.getLatestLotteryResults()
            if (response.isSuccessful) {
                response.body()?.let { NetworkResult.Success(it) } ?: NetworkResult.Error("Empty Response")
            } else {
                NetworkResult.Error(response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.localizedMessage ?: "Unknown Error")
        }
    }

    fun getAllUsersPaged(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { userDao.getAllUsersPaged() }
        ).flow
    }
}
