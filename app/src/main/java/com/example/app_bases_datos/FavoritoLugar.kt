package com.example.app_bases_datos

import android.os.Parcel
import android.os.Parcelable

data class FavoritoLugar(
    val nombre: String = "",
    val direccion: String = "",
    val tiempo: Int = 0,
    val imagenURL: String = "",
    val precio: Int = 0,
    val categoria: String = "",
    val descripcion: String = "",
)