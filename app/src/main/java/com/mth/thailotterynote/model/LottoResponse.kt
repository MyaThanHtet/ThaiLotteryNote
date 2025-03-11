package com.mth.thailotterynote.model

data class LottoResponse(
    val status: String,
    val response: ResponseData
)

data class ResponseData(
    val date: String,
    val endpoint: String,
    val prizes: List<Prize>,
    val runningNumbers: List<RunningNumber>
)

data class Prize(
    val id: String,
    val name: String,
    val reward: String,
    val amount: Int,
    val number: List<String>
)

data class RunningNumber(
    val id: String,
    val name: String,
    val reward: String,
    val amount: Int,
    val number: List<String>
)