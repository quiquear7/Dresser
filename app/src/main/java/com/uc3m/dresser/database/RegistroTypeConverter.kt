package com.uc3m.dresser.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class RegistroTypeConverter {
    @TypeConverter
    open fun lisiToString(list: Combinacion): String {
        val gson = Gson()
        val type = object : TypeToken<Combinacion>() {

        }.type
        val json = gson.toJson(list, type)

        return json
    }

    @TypeConverter
    open fun StringToArray(value: String): Combinacion {

        val gson = Gson()
        val type = object : TypeToken<Combinacion>() {

        }.type
        return gson.fromJson(value, type)

    }
}