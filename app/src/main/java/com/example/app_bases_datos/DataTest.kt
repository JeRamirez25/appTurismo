package com.example.app_bases_datos

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app_bases_datos.utils.*
import kotlinx.coroutines.launch

class DataTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Pruebas
        val ID_USUARIO = "qDKCX43GUHbA0pyB8yRl"
        val NOMBRE_RUTA = "EJEMPLO 1"
        val ID_RUTA = "uUYedKiCO3puSTKZ2H4E"
        val ID_LUGAR = "0osp1XdSPtCjtoVDdAeg"

        // LUGARES A FAVS

        //añadirLugar(ID_USUARIO,ID_LUGAR)
        //eliminarLugar(ID_USUARIO,ID_LUGAR)

        // RUTAS

        lifecycleScope.launch {
            //Log.d("Firestore", "Hay rutas en el usuario? ${verificarRuta(ID_USUARIO)}")
            //crearRuta(ID_USUARIO,NOMBRE_RUTA)
            //eliminarRuta(ID_USUARIO,ID_RUTA)
            //Log.d("Firestore", "Hay rutas en el usuario? ${verificarRuta(ID_USUARIO)}")
        }
        //obtenerIdUsuario("kevinaxelhr18@gmail.com")
        // LUGARES A RUTAS

        //añadirLugarRutas(ID_RUTA,ID_LUGAR)
        //eliminarLugarRutas(ID_RUTA,ID_LUGAR)

        //crearUsuario("holaaa@gmail.com", "Sara")
        //getDetallesRuta("8GIG2kWspsgproppONRL")
        //getDetallesLugares("8GIG2kWspsgproppONRL")

        //crearLugar(
        //    nombre = "Jardín Botánico de Bogotá José Celestino Mutis",
        //    categoria = "Históricos",
        //    descripcion = "Jardín Botánico de Bogotá José Celestino Mutis",
        //    direccion = "Jardín Botánico de Bogotá José Celestino Mutis",
        //    imagenURL = "Jardín Botánico de Bogotá José Celestino Mutis",
        //    precio = 0,
        //    tiempo = 0
        //)
    }

}

