package com.mth.thailotterynote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "dashboard_history")
data class DashboardHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "due_date") val dueDate: String,
    @ColumnInfo(name = "total_sold_count") val totalSoldCount: Int,
    @ColumnInfo(name = "number_of_paid") val numberOfPaid: Int,
    @ColumnInfo(name = "number_of_unpaid") val numberOfUnpaid: Int,
    @ColumnInfo(name = "total_price_mmk") val totalPriceMMK: Double,
    @ColumnInfo(name = "total_price_thb") val totalPriceTHB: Double,
    @ColumnInfo(name = "num_ticket_mmk") val numTicketMMK: Int,
    @ColumnInfo(name = "num_ticket_thb") val numTicketTHB: Int
)