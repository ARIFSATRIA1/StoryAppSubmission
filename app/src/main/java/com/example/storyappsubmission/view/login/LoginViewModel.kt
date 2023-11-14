package com.example.storyappsubmission.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission.data.preferences.TokenModel
import com.example.storyappsubmission.data.repository.Repository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository): ViewModel() {

    /*
        Function to fetch data from api Login
     */
    fun loginUser(email: String, password: String) = repository.loginUser(email,password)

    /*
        Function to get Repository To Delete Token From Data Store
     */
    fun saveToken(token:TokenModel) {
        viewModelScope.launch {
            repository.saveToken(token)
        }
    }

    /*
        Function To Get Token From Repository
     */

    fun getToken(): LiveData<TokenModel> {
        return repository.getToken().asLiveData()
    }

}