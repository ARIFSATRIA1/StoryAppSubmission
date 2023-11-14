package com.example.storyappsubmission.data

sealed class ResultState<out R> private constructor(){
    data class Sucsess<out T>(val data: T): ResultState<T>()
    data class Error(val error: String): ResultState<Nothing>()
    object Loading: ResultState<Nothing>()
}