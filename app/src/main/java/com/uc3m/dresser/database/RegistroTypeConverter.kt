package com.uc3m.dresser.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RegistroTypeConverter {
    @TypeConverter
    open fun lisiToString(list: List<Prenda>): String {
        if (list == null || list.size === 0) {
            return ""
        }
        val gson = Gson()
        val type = object : TypeToken<List<Prenda>>() {

        }.type
        val json = gson.toJson(list, type)

        return json
    }

    @TypeConverter
    open fun StringToArray(value: String): List<Prenda> {

        val gson = Gson()
        val type = object : TypeToken<List<Prenda>>() {

        }.type
        return gson.fromJson(value, type)

    }
}