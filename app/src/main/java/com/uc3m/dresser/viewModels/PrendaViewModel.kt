package com.uc3m.dresser.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.uc3m.dresser.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class PrendaViewModel (application: Application): AndroidViewModel(application){
    val lastOutfit: LiveData<Registro>
    val readAll: LiveData<List<Prenda>>
    private val repository: PrendaRepository

    init {
        val prendaDAO = PrendaDatabase.getDatabase(application).prendaDao()
        repository = PrendaRepository(prendaDAO)
        readAll = repository.readAll
        lastOutfit = repository.readLastOutfit
    }

    fun addPrenda(prenda: Prenda){
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

    fun readOcasion(ocasion: String, currentDate: Long): LiveData<List<Prenda>>{
        return repository.readOcasion(ocasion, currentDate)
    }

    fun deletePrenda(prenda: Prenda) {
        viewModelScope.launch(Dispatchers.IO){
            repository.deletePrenda(prenda)
        }

    }

    fun readId(id: Int): LiveData<Prenda> {
        return repository.readId(id)
    }

    fun updatePrenda(prenda: Prenda) {
        viewModelScope.launch(Dispatchers.IO){
            repository.updatePrenda(prenda)
        }
    }
}