package com.uc3m.dresser.ui

import com.uc3m.dresser.database.Registro
import java.util.*

interface SendData {

    fun sendInfo(registro: Registro, date: Date?);
}