package com.mth.thailotterynote.database.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mth.thailotterynote.model.DashboardHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface DashboardHistoryDao {

    @Insert
    suspend fun insert(dashboardHistory: DashboardHistory)

    @Update
    suspend fun update(dashboardHistory: DashboardHistory)

    @Query("SELECT * FROM dashboard_history WHERE due_date = :date LIMIT 1")
    fun getDashboardHistoryForDate(date: String): Flow<DashboardHistory?>

    @Delete
    suspend fun delete(dashboardHistory: DashboardHistory)


    @Transaction
    suspend fun updateHistory(oldHistory: DashboardHistory, newHistory: DashboardHistory) {
        delete(oldHistory)
        insert(newHistory)
    }
}