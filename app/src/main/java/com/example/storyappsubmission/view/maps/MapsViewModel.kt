package com.example.storyappsubmission.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyappsubmission.data.ResultState
import com.example.storyappsubmission.data.preferences.TokenModel
import com.example.storyappsubmission.data.remote.response.AllStoryResponse
import com.example.storyappsubmission.data.repository.Repository
import java.util.concurrent.Flow

class MapsViewModel(private val repository: Repository): ViewModel() {

    fun getStoriesWithLocation(token:String) = repository.getStoryWithLocation(token)

    fun getToken(): LiveData<TokenModel> {
        return repository.getToken().asLiveData()
    }

}