package com.uc3m.dresser.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_registro")
data class Registro(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        //val prenda: List<Prenda>,
        val fecha: String
)


