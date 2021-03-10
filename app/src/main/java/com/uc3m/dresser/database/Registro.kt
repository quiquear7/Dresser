package com.uc3m.dresser.database

import androidx.room.*

@Entity(tableName = "table_registro")
data class Registro(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val fecha: String,
        @TypeConverters(RegistroTypeConverter::class)
        val prenda: List<Prenda>
)


