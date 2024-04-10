package com.example.nychighschools.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "school_with_sat_scores")
data class NYCHighSchools(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("school_name")
    val schoolName: String?,
    @SerializedName("num_of_sat_test_takers")
    val numOfSatTestTakers: String?,
    @SerializedName("sat_critical_reading_avg_score")
    val satCriticalReadingAvgScore: String?,
    @SerializedName("sat_math_avg_score")
    val satMathAvgScore: String?,
    @SerializedName("sat_writing_avg_score")
    val satWritingAvgScore: String?
)
