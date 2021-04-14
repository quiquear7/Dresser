package com.uc3m.dresser.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity (tableName = "table_prenda")
data class Prenda(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombre: String,
    val categoria: String,
    val color: String,
    val estampado: String,
    val ocasion: String,
    @TypeConverters(DateConverter::class)
    var ultimoUso: Long,
    val iv: String,
    val encryptedRuta: String
)

