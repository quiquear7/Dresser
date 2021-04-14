package com.uc3m.dresser.database

import androidx.lifecycle.LiveData
import java.time.LocalDate
import java.util.*

class PrendaRepository (private val prendaDao: PrendaDao) {

    val readAll: LiveData<List<Prenda>> = prendaDao.readAll()



    suspend fun addPrenda (prenda: Prenda){
        prendaDao.addPrenda(prenda)
    }

    suspend fun addRegistro (registro: Registro){
        prendaDao.addRegistro(registro)
    }

     fun readDate(date: String): LiveData<List<Registro>> {
        return prendaDao.readDate(date)
    }

    fun readOcasion(ocasion: String, currentDate: Long): LiveData<List<Prenda>> {
        return prendaDao.readOcasion(ocasion, currentDate)
    }

    suspend fun deletePrenda(prenda: Prenda) {
        prendaDao.deletePrenda(prenda)

    }

    fun readId(id: Int): LiveData<Prenda> {
        return prendaDao.readId(id)
    }

    suspend fun updatePrenda(prenda: Prenda) {
        prendaDao.updatePrenda(prenda)
    }

    val readLastOutfit: LiveData<Registro> = prendaDao.readLastOutfit()
}