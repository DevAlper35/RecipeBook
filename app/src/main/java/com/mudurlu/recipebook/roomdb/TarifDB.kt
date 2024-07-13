package com.mudurlu.recipebook.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mudurlu.recipebook.model.Tarif

@Database(entities = [Tarif::class], version = 1)
abstract class TarifDB: RoomDatabase() {
    abstract fun tarifDAO() : TarifDAO
}