package com.mth.thailotterynote.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val date: String,
    val ticketNumber: String,
    val currency: List<String>,
    val isPaid: Boolean,
    val lotteryDate: String,
    val price:Int
)