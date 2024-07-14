package com.mudurlu.recipebook.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mudurlu.recipebook.model.Tarif
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface TarifDAO {

    @Query("SELECT * FROM tarif")
    fun tumTarifler() : Flowable<List<Tarif>>

    @Query("SELECT * FROM tarif WHERE id = :id")
    fun tarifGetir(id : Int) : Flowable<Tarif>

    @Insert
    fun tarifEkle(tarif : Tarif) : Completable

    @Delete
    fun tarifSil(tarif : Tarif) : Completable

}