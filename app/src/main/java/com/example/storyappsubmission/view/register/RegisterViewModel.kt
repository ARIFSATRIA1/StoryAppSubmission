package com.example.storyappsubmission.view.register

import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.data.repository.Repository

class RegisterViewModel(private val repository: Repository):ViewModel() {

    /*
        Function To Post Api From Fetching Data Repository
     */
    fun registerUsers(name: String,email: String,password: String) = repository.register(name, email, password)
}