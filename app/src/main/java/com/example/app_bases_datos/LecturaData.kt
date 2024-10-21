package com.example.app_bases_datos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app_bases_datos.utils.getDetallesRuta as getDetallesRuta
import com.example.app_bases_datos.utils.getDetallesLugares as getDetallesLugares
import com.example.app_bases_datos.utils.crearRuta as crearRuta
import com.example.app_bases_datos.utils.crearUsuario as crearUsuario
import com.example.app_bases_datos.utils.updateRutasFavoritas as updateRutasFavoritas
import com.example.app_bases_datos.utils.updateLugaresFavoritos as updateLugaresFavoritos
import com.example.app_bases_datos.utils.updateLugaresDeRuta as updateLugaresDeRuta

class LecturaData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //crearUsuario("holaaa@gmail.com", "Sara")
        crearRuta("Ruta Hola")
        //updateLugaresDeRuta("B07MOxIZT3J4ZRtRrAue","j75UBGTz64DvGhG1YB2Z")
        //updateRutasFavoritas("8GIG2kWspsgproppONRL","aimxc5Nbk7yGKZP26om5")
        //updateLugaresFavoritos("8GIG2kWspsgproppONRL","Ye2zT3Rv8iw1f67dsliF")
        getDetallesRuta("8GIG2kWspsgproppONRL")
        getDetallesLugares("8GIG2kWspsgproppONRL")

    }

}
