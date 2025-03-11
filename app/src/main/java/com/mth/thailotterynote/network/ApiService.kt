package com.mth.thailotterynote.network

import com.mth.thailotterynote.model.LottoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("latest")
    suspend fun getLatestLotteryResults(): Response<LottoResponse>

    @GET("check/{date}")
    fun getLotteryResultsForDate(@Path("date") date: String): Response<LottoResponse>
}