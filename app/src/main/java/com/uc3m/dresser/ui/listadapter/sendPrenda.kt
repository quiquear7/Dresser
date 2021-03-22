package com.uc3m.dresser.ui.listadapter

import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.database.Registro

interface SendPrenda {
    fun sendInfo(prenda: Prenda, operacion: Int);
}