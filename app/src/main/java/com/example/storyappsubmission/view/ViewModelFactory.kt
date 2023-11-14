package com.example.storyappsubmission.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.data.di.Injection
import com.example.storyappsubmission.data.repository.Repository
import com.example.storyappsubmission.view.detail.DetailViewModel
import com.example.storyappsubmission.view.login.LoginViewModel
import com.example.storyappsubmission.view.main.MainViewModel
import com.example.storyappsubmission.view.maps.MapsViewModel
import com.example.storyappsubmission.view.register.RegisterViewModel
import com.example.storyappsubmission.view.story.StoryViewModel

class ViewModelFactory(private val repository: Repository):ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return when {
           modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
               LoginViewModel(repository) as T
           }
           modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
               RegisterViewModel(repository) as T
           }
           modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
               DetailViewModel(repository) as T
           }
           modelClass.isAssignableFrom(MainViewModel::class.java) -> {
               MainViewModel(repository) as T
           }
           modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
               StoryViewModel(repository) as T
           }
           modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
               MapsViewModel(repository) as T
           }
           else -> throw IllegalArgumentException("Unknow ViewModel Class: " + modelClass.name)
       }

    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (instance == null) {
                synchronized(ViewModelFactory::class.java) {
                    instance = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return instance as ViewModelFactory
        }

    }

}