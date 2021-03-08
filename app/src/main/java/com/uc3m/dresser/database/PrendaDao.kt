package com.uc3m.dresser.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PrendaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPrenda(prenda: Prenda)

    @Query ("SELECT * FROM table_prenda ORDER BY id ASC")
    fun readAll(): LiveData<List<Prenda>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRegistro(registro: Registro)

    @Query ("SELECT * FROM table_registro WHERE fecha = :fecha ")
    fun readDate(fecha: String): LiveData<List<Registro>>
}