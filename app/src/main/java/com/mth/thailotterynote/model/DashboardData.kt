package com.mth.thailotterynote.model

data class DashboardData(
    val paidCount: Int,
    val totalCount: Int,
    val totalTicketsSold: Int,
    val totalAmountMMK: Double,
    val mmkTicketsSold: Int,
    val totalAmountTHB: Double,
    val thbTicketsSold: Int
)