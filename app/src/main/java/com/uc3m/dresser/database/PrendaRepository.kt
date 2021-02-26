package com.uc3m.dresser.database

import androidx.lifecycle.LiveData

class PrendaRepository (private val prendaDao: PrendaDao) {

    val readAll: LiveData<List<Prenda>> = prendaDao.readAll()

    suspend fun addPrenda (prenda: Prenda){
        prendaDao.addPrenda(prenda)
    }
//hola
}