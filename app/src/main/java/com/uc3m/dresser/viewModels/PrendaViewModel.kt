package com.uc3m.dresser.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.uc3m.dresser.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrendaViewModel (application: Application): AndroidViewModel(application){

    val readAll: LiveData<List<Prenda>>
    private val repository: PrendaRepository

    init {
        val prendaDAO = PrendaDatabase.getDatabase(application).prendaDao()
        repository = PrendaRepository(prendaDAO)
        readAll = repository.readAll
    }

    fun addStudent(prenda: Prenda){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPrenda(prenda)
        }
    }

    fun addRegistro(registro: Registro){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRegistro(registro)
        }
    }

    fun readDate(date: String): LiveData<List<Registro>>{
        return repository.readDate(date)
    }
}