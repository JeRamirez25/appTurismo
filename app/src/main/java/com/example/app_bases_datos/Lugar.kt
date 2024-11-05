package com.example.app_bases_datos

data class Lugar(
    val nombre: String = "",
    val direccion: String = "",
    val tiempo: Int = 0,
    val imagenURL: String = "",
    val precio: Int = 0,
    val descripcion: String = "",
    var categoria: String = "",
) {
    var id: String = ""
}