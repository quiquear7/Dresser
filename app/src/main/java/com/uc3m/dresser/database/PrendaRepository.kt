package com.uc3m.dresser.database

import androidx.lifecycle.LiveData

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
}