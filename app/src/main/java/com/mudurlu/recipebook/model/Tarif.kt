package com.mudurlu.recipebook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tarif (

    @ColumnInfo(name = "yemek_adi")
    var yemek_adi : String,

    @ColumnInfo(name = "yemek_malzemeleri")
    var yemek_malzemeleri : String,

    @ColumnInfo(name = "gorsel")
    var gorsel : ByteArray

){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}