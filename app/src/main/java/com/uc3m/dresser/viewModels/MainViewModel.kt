package com.uc3m.dresser.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uc3m.dresser.model.Clima
import com.uc3m.dresser.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response


class MainViewModel(private val repository: Repository): ViewModel() {
    val myResponse: MutableLiveData<Response<Clima>> = MutableLiveData()

    fun getClima(s: Map<String, String>) {
        viewModelScope.launch{
            val response = repository.getClima(s)
            myResponse.value = response
        }
    }
}