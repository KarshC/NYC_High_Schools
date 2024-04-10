package com.example.nychighschools.di

import android.content.Context
import androidx.room.Room
import com.example.nychighschools.data.db.NYCSchoolDao
import com.example.nychighschools.data.db.NYCSchoolsDatabase
import com.example.nychighschools.data.repository.NYCSchoolsRepository
import com.example.nychighschools.data.service.NYCSchoolApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSchoolDatabase(@ApplicationContext context: Context): NYCSchoolsDatabase =
        Room.databaseBuilder(
            context, NYCSchoolsDatabase::class.java, "nyc_school_database"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideSchoolDao(database: NYCSchoolsDatabase): NYCSchoolDao {
        return database.getNycSchoolsDao()
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggingInterceptor
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideNycSchoolApi(okHttpClient: OkHttpClient): NYCSchoolApi {
        return Retrofit.Builder()
            .baseUrl("https://data.cityofnewyork.us/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NYCSchoolApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNycSchoolRepository(
        nycSchoolApi: NYCSchoolApi,
        nycSchoolDao: NYCSchoolDao
    ): NYCSchoolsRepository {
        return NYCSchoolsRepository(nycSchoolApi, nycSchoolDao)
    }
}

