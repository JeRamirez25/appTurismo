package com.example.app_bases_datos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app_bases_datos.utils.crearLugar as crearLugar

class DataTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //crearUsuario("holaaa@gmail.com", "Sara")
        //crearRuta("Ruta Hola")
        //updateLugaresDeRuta("B07MOxIZT3J4ZRtRrAue","j75UBGTz64DvGhG1YB2Z")
        //updateRutasFavoritas("8GIG2kWspsgproppONRL","aimxc5Nbk7yGKZP26om5")
        //updateLugaresFavoritos("8GIG2kWspsgproppONRL","Ye2zT3Rv8iw1f67dsliF")
        //getDetallesRuta("8GIG2kWspsgproppONRL")
        //getDetallesLugares("8GIG2kWspsgproppONRL")

        crearLugar(
            nombre = "Jardín Botánico de Bogotá José Celestino Mutis",
            categoria = "Históricos",
            descripcion = "Jardín Botánico de Bogotá José Celestino Mutis",
            direccion = "Jardín Botánico de Bogotá José Celestino Mutis",
            imagenURL = "Jardín Botánico de Bogotá José Celestino Mutis",
            precio = 0,
            tiempo = 0
        )
    }

}

