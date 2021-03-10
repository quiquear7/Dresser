package com.uc3m.dresser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database (entities = [Prenda::class, Registro::class], version = 1, exportSchema = false)
@TypeConverters(RegistroTypeConverter::class)
abstract class PrendaDatabase: RoomDatabase() {

    abstract fun prendaDao(): PrendaDao

    companion object {
        @Volatile
        private var INSTANCE: PrendaDatabase? = null

        fun getDatabase(context: Context): PrendaDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PrendaDatabase::class.java,
                        "prenda_database"
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }
    }
}