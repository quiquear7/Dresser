package com.uc3m.dresser.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "table_prenda")
data class Prenda(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val categoria: String?,
    val color: String?,
    val ruta: String?
)