package com.example.nychighschools.data.repository

import com.example.nychighschools.data.model.NYCHighSchools
import com.example.nychighschools.util.Result
import kotlinx.coroutines.flow.Flow

interface NYCSchoolRepository {
    fun getSchoolDataFlow(): Flow<Result<List<NYCHighSchools>>>
    suspend fun refreshSchoolData()

}