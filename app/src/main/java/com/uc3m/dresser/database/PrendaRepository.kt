package com.uc3m.dresser.database

import androidx.lifecycle.LiveData

class PrendaRepository (private val prendaDao: PrendaDao) {

    val readAll: LiveData<List<Prenda>> = prendaDao.readAll()

    suspend fun addUser (prenda: Prenda){
        prendaDao.addPrenda(prenda)
    }

    fun addPrenda(prenda: Prenda) {

    }
}