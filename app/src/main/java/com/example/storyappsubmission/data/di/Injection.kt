package com.example.storyappsubmission.data.di


import android.content.Context
import com.example.storyappsubmission.data.database.StoryDataBase
import com.example.storyappsubmission.data.preferences.TokenPreferences
import com.example.storyappsubmission.data.preferences.dataStore
import com.example.storyappsubmission.data.remote.retrofit.ApiConfig
import com.example.storyappsubmission.data.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = TokenPreferences.getInstance(context.dataStore)
        val database = StoryDataBase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService,pref,database)
    }
}