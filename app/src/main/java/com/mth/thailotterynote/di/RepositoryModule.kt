package com.mth.thailotterynote.di

import com.mth.thailotterynote.database.dao.DashboardHistoryDao
import com.mth.thailotterynote.database.dao.UserDao
import com.mth.thailotterynote.network.ApiService
import com.mth.thailotterynote.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideUserRepository(
        userDao: UserDao,
        dashboardHistoryDao: DashboardHistoryDao,
        apiService: ApiService
    ): UserRepository {
        return UserRepository(userDao, dashboardHistoryDao,apiService)
    }


}