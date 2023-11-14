package com.example.storyappsubmission.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyappsubmission.data.preferences.TokenModel
import com.example.storyappsubmission.data.remote.response.DetailStoryResponse
import com.example.storyappsubmission.data.remote.response.Story
import com.example.storyappsubmission.data.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: Repository) : ViewModel() {

    private val _users = MutableLiveData<Story>()
    val user: LiveData<Story> = _users

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getToken(): LiveData<TokenModel> {
        return repository.getToken().asLiveData()
    }

    /*
        Function To Process Data Result
     */
    fun detailStory(token: String,id: String){
        _loading.value = true
        val client = repository.detailStory(token,id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    _loading.value = false
                    _users.value = response.body()?.story as Story
                } else {
                    Log.d(TAG,"onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _loading.value = false
                Log.d(TAG,"onFailure ${t.message.toString()}")
            }

        })
    }

    companion object {
        const val TAG = "DetailViewModel"
    }

}