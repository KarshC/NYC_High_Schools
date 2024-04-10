package com.example.nychighschools.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nychighschools.data.model.NYCHighSchools

@Database(entities = [NYCHighSchools::class], version = 2, exportSchema = false)
abstract class NYCSchoolsDatabase: RoomDatabase() {
    abstract fun getNycSchoolsDao(): NYCSchoolDao
}