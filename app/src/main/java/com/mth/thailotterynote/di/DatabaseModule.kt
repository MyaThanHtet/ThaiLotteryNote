package com.mth.thailotterynote.di

import android.content.Context
import androidx.room.Room
import com.mth.thailotterynote.database.LotteryDataBase
import com.mth.thailotterynote.database.dao.DashboardHistoryDao
import com.mth.thailotterynote.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LotteryDataBase {
        return Room.databaseBuilder(
            context,
            LotteryDataBase::class.java,
            "lottery_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: LotteryDataBase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideDashboardHistoryDao(database: LotteryDataBase): DashboardHistoryDao {
        return database.dashboardHistoryDao()
    }
}