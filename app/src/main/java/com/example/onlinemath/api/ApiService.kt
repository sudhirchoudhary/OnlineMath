package com.example.onlinemath.api

import com.example.onlinemath.ExpressionResponse
import com.example.onlinemath.ExpressionsRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("v4/")
    suspend fun getResult(@Query("expr") expr: String) : Response<String>

    @POST("v4/")
    suspend fun getPostResult(@Body expressionsRequest: ExpressionsRequest): Response<ExpressionResponse>
}