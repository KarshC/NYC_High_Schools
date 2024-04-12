package com.example.nychighschools.data.service

import com.example.nychighschools.data.model.NYCHighSchools
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NYCSchoolApi {

    @GET("resource/f9bf-2cp4.json")
    suspend fun getSchoolWithSatScores(
        @Query("\$\$app_token") appToken: String = "8K6T7qeQ0eXRcilVJTpTnXYxT"
    ): Response<List<NYCHighSchools>>
}