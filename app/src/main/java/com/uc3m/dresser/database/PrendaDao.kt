package com.uc3m.dresser.database

import androidx.lifecycle.LiveData
import androidx.room.*
import retrofit2.http.DELETE

@Dao
interface PrendaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPrenda(prenda: Prenda)

    @Query ("SELECT * FROM table_prenda ORDER BY id ASC")
    fun readAll(): LiveData<List<Prenda>>

    @Query ("SELECT * FROM table_prenda WHERE ocasion = :ocasion")
    fun readOcasion(ocasion: String): LiveData<List<Prenda>>

    @Query ("SELECT * FROM table_prenda WHERE id = :id")
    fun readId(id: Int): LiveData<Prenda>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRegistro(registro: Registro)

    @Query ("SELECT * FROM table_registro WHERE fecha = :fecha ")
    fun readDate(fecha: String): LiveData<List<Registro>>

    @Query("SELECT * FROM table_registro ORDER BY id DESC LIMIT 1")
    fun readLastOutfit(): LiveData<Registro>

    @Delete()
    suspend fun deletePrenda(prenda: Prenda)

    @Update(entity =  Prenda::class)
    suspend fun updatePrenda(prenda: Prenda)
}