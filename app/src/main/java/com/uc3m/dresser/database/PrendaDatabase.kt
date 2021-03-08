package com.uc3m.dresser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [Prenda::class, Registro::class], version = 1, exportSchema = false)
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