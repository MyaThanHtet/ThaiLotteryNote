package com.mth.thailotterynote.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mth.thailotterynote.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT DISTINCT lotteryDate FROM User")
    fun getAllLotteryDates(): Flow<List<String>>

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM User WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM User WHERE lotteryDate = :lotteryDate")
     fun getUsersByLotteryDate(lotteryDate: String): Flow<List<User>>

    @Query("SELECT * FROM User ORDER BY id ASC")
    fun getAllUsersPaged(): PagingSource<Int, User>
}