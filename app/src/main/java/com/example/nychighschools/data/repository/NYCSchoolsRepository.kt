package com.example.nychighschools.data.repository

import android.util.Log
import com.example.nychighschools.data.db.NYCSchoolDao
import com.example.nychighschools.data.model.NYCHighSchools
import com.example.nychighschools.data.service.NYCSchoolApi
import com.example.nychighschools.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NYCSchoolsRepository @Inject constructor(
    private val nycSchoolApi: NYCSchoolApi,
    private val nycSchoolDao: NYCSchoolDao
) {

    fun getSchoolDataFlow(): Flow<Result<List<NYCHighSchools>>> = flow {
        emit(Result.Loading)
        try {
            refreshSchoolData()
        }catch (e: Exception){
            emit(Result.Error(e))
        }

        nycSchoolDao.getAllSchools().collect{ schools ->
            emit(Result.Success(schools))
        }
    }

    suspend fun refreshSchoolData() {

        return withContext(Dispatchers.IO) {
            try {
                val newSchoolData = nycSchoolApi.getSchoolWithSatScores()
                Log.d("Repository", "Fetched Data: $newSchoolData")
                newSchoolData.body()?.let { nycSchoolDao.insertAllSchools(it) }
            } catch (e: Exception) {
                Log.e("TAG_SCHOOL_API", "Api error")
            }
        }
    }
}