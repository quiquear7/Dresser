package com.uc3m.dresser.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "table_registro")
data class Registro(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        //val prenda: List<Prenda>  = listOf() ,
        val fecha: String
)


