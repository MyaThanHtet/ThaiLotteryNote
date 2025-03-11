package com.mth.thailotterynote.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mth.thailotterynote.database.dao.DashboardHistoryDao
import com.mth.thailotterynote.database.dao.UserDao
import com.mth.thailotterynote.model.DashboardHistory
import com.mth.thailotterynote.model.User
import com.mth.thailotterynote.utils.Converters

@Database(entities = [User::class, DashboardHistory::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LotteryDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun dashboardHistoryDao(): DashboardHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: LotteryDataBase? = null

        fun getDatabase(context: Context): LotteryDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, LotteryDataBase::class.java, "lottery_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}