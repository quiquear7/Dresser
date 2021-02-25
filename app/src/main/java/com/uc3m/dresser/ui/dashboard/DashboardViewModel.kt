package com.uc3m.dresser.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Agregar Nueva Prenda de Ropa"
    }
    val text: LiveData<String> = _text
}