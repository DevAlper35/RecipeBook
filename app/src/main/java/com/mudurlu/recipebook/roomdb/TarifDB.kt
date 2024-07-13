package com.mudurlu.recipebook.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mudurlu.recipebook.model.Tarif

@Database(entities = [Tarif::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun tarifDAO() : TarifDAO
}