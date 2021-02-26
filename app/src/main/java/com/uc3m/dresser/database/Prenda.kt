package com.uc3m.dresser.database

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "table_prenda")

data class Prenda (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val categoria: String,
    val color: String,
    var foto: Uri
)