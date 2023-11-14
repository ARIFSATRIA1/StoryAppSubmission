package com.example.storyappsubmission.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyappsubmission.data.enitity.StoryEntity
import com.example.storyappsubmission.data.preferences.TokenModel
import com.example.storyappsubmission.data.remote.response.ListStoryItem
import com.example.storyappsubmission.data.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _users = MutableLiveData<List<ListStoryItem>>()
    val users: LiveData<List<ListStoryItem>> = _users

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    /*
        Function To Get Token From Repository
     */
    fun getToken(): LiveData<TokenModel> {
        return repository.getToken().asLiveData()
    }


    /*
        Function To Delete Session From DataStore
     */
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun saveToken(token: TokenModel) {
        viewModelScope.launch {
            repository.saveToken(token)
        }
    }

    /*
        Function To getStories from fetching Api Response
     */
    fun getAllStories(token: String): LiveData<PagingData<StoryEntity>> =
        repository.getStory(token).cachedIn(viewModelScope)

    companion object {
        const val TAG = "MainViewModel"

    }

}