package com.mudurlu.recipebook.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mudurlu.recipebook.model.Tarif

@Dao
interface TarifDAO {

    @Query("SELECT * FROM tarif")
    fun tumTarifler() : List<Tarif>

    @Query("SELECT * FROM tarif WHERE id = :id")
    fun tarifGetir(id : Int) : Tarif

    @Insert
    fun tarifEkle(tarif : Tarif)

    @Delete
    fun tarifSil(tarif : Tarif)

}