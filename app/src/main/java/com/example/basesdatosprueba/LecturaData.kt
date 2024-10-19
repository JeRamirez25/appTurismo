package com.example.basesdatosprueba

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

    class LecturaData : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_main)

            val db = Firebase.firestore

            db.collection("lugares")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {

                        // Para visualizar todos los datos de una vez, descomentarlo pa' ver
                        //Log.d(TAG, "${document.id} => ${document.data}")

                        // Para traer cada dato uno a uno
                        val nombre = document.getString("nombre")
                        val descripcion = document.getString("descripcion")
                        val direccion = document.getString("direccion")
                        val imagenurl = document.getString("imagenURL")
                        val precio = document.getLong("precio")?.toInt()
                        val tiempo = document.getLong("tiempo")?.toInt()
                        val geopoint = document.getGeoPoint("geopint")

                        // esto es para separar latitud de longitud jeje
                        if (geopoint != null) {
                            val latitud = geopoint.latitude
                            val longitud = geopoint.longitude
                        }

                        //Toast.makeText(this, "ID: ${document.id}, Data: ${document.data}", Toast.LENGTH_LONG).show()
                        Log.d(TAG, "El nombre es: ${nombre}, la descripcion es:  ${descripcion}, y su direccion es  ${direccion}. precio:  ${precio}, tiempo de visita: ${tiempo} ")

                    }

                    // Muestra en la app el toast si funcionón
                    Toast.makeText(
                        this,
                        "Terminó correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                }

                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)

                    // Toast por si se petatea la vaina
                    Toast.makeText(
                        this,
                        "se petateo",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
    }
