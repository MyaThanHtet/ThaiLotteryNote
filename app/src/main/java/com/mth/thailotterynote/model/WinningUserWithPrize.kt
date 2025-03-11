package com.mth.thailotterynote.model

data class WinningUserWithPrize(
    val user: User,
    val prizeName: String,
    val ticketNumber:String,
    val reward:String,
    val date:String
)
