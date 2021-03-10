package com.uc3m.dresser.ui

import com.uc3m.dresser.database.Registro

interface SendData {
    fun sendInfo(registro: Registro);
}