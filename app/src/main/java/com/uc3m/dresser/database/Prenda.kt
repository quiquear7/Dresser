package com.uc3m.dresser.database

import android.text.Editable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "table_prenda")
data class Prenda(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombre: String,
    val categoria: String,
    val color: String,
    val estampado: String,
    val ocasion: String,
    val iv: String,
    val encryptedRuta: String
)

