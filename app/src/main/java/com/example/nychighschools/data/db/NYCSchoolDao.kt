package com.example.nychighschools.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nychighschools.data.model.NYCHighSchools
import kotlinx.coroutines.flow.Flow

@Dao
interface NYCSchoolDao {

    @Query("SELECT * FROM school_with_sat_scores")
    fun getAllSchools(): Flow<List<NYCHighSchools>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSchools(schools: List<NYCHighSchools>)
}