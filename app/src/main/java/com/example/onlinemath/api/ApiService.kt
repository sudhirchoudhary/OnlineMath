package com.example.onlinemath.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v4/")
    suspend fun getResult(@Query("expr") expr: String) : Response<String>
}