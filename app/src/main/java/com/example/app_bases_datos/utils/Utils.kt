package com.example.app_bases_datos.utils

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.GeoPoint

// En esta función se crean los usuarios
fun crearUsuario(correo: String, nombre: String) {
    val db = Firebase.firestore
    val usuario = hashMapOf(
        "id" to correo,
        "nombre" to nombre,
        "lugaresFavoritos" to emptyList<String>(),
        "rutasFavoritas" to emptyList<String>()
    )

    db.collection("usuarios").add(usuario)
        .addOnSuccessListener {
            Log.d("Firestore", "Usuario creado correctamente")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al crear usuario", e)
        }
}

// En esta función se crean nuevas rutas
fun crearRuta(nombreRuta: String) {
    val db = Firebase.firestore
    val ruta = hashMapOf(
        "nombre" to nombreRuta,
        "lugares" to emptyList<String>()
    )

    db.collection("rutas").add(ruta)

        .addOnSuccessListener {
            Log.d("Firestore", "Ruta creada correctamente")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al crear ruta", e)
        }
}

// En esta función se updatean los lugares favoritos de los usuarios
fun updateLugaresFavoritos(idUsuario: String, idLugar: String) {
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection("usuarios").document(idUsuario)

    usuarioRef.update("lugaresFavoritos", FieldValue.arrayUnion(idLugar))
        .addOnSuccessListener {
            Log.d("Firestore", "Lugar guardado correctamente en los favoritos usuario")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al actualizar lugares favoritos", e)
        }
}

// En esta función se updatean las rutas favs de los usuarios
fun updateRutasFavoritas(idUsuario: String, idRuta: String) {
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection("usuarios").document(idUsuario)

    usuarioRef.update("rutasFavoritas", FieldValue.arrayUnion(idRuta))
        .addOnSuccessListener {
            Log.d("Firestore", "la ruta ${idRuta} se ha añadida correctamente")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al actualizar rutas favoritas", e)
        }
}

// En esta función se updatean los lugares en las rutas
fun updateLugaresDeRuta(idRuta: String, idLugar: String) {
    val db = Firebase.firestore
    val rutaRef = db.collection("rutas").document(idRuta)

    rutaRef.update("lugares", FieldValue.arrayUnion(idLugar))
        .addOnSuccessListener {
            Log.d("Firestore", "Lugar añadido a la ruta ${idRuta}")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al añadir lugar a la ruta", e)
        }
}

fun getDetallesLugares(idUsuario: String) {
    // Recorrer cada ID de lugar para obtener su información
    val db = Firebase.firestore
    db.collection("usuarios").document(idUsuario)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val lugaresFavoritos = document.get("lugaresFavoritos") as? List<String>
                if (lugaresFavoritos != null) {
                    //función para obtener los detalles de cada lugar
                    for (lugar in lugaresFavoritos) {
                        db.collection("lugares").document(lugar).get()
                            .addOnSuccessListener { lugarDoc ->
                                if (lugarDoc.exists()) {
                                    val nombre = lugarDoc.getString("nombre")
                                    val descripcion = lugarDoc.getString("descripcion")
                                    val direccion = lugarDoc.getString("direccion")
                                    val precio = lugarDoc.getLong("precio")?.toInt()
                                    val tiempo = lugarDoc.getLong("tiempo")?.toInt()

                                    Log.d(
                                        "Firestore",
                                        "Lugar favorito: $nombre, Descripción: $descripcion, Dirección: $direccion, Precio: $precio, Tiempo de visita: $tiempo"
                                    )
                                } else {
                                    Log.d("Firestore", "Lugar con ID $lugar no encontrado.")
                                }
                            }

                            .addOnFailureListener { e ->
                                Log.e(
                                    "Firestore",
                                    "Error al obtener detalles del lugar con ID $lugar",
                                    e
                                )
                            }
                    }
                }
            }
        }
}

fun getDetallesRuta(idUsuario: String) {
    val db = Firebase.firestore
    db.collection("usuarios")
        .document(idUsuario).get().addOnSuccessListener { document ->
            if (document.exists()) {
                val rutasFavoritas = document.get("rutasFavoritas") as? List<String>
                if (rutasFavoritas != null) {
                    for (rutaId in rutasFavoritas) {
                        db.collection("rutas").document(rutaId)
                            .get()
                            .addOnSuccessListener { lugarDoc ->
                                if (lugarDoc.exists()) {
                                    val nombre = lugarDoc.getString("nombre")
                                    Log.d("Firestore", "Nombre: $nombre")
                                } else {
                                    Log.d("Firestore", "Ruta con ID $rutaId no encontrado.")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e(
                                    "Firestore",
                                    "Error al obtener detalles de la ruta con ID $rutaId",
                                    e
                                )
                            }
                    }
                }
            }

        }
}

fun crearLugar(
    nombre: String,
    categoria: String,
    descripcion: String,
    direccion: String,
    imagenURL: String,
    precio: Int,
    tiempo: Int
) {
    val db = FirebaseFirestore.getInstance()
    val lugar = hashMapOf(
        "categoria" to categoria,
        "descripcion" to descripcion,
        "direccion" to direccion,
        "geopoint" to null,
        "imagenURL" to imagenURL,
        "nombre" to nombre,
        "precio" to precio,
        "tiempo" to tiempo
    )

    db.collection("lugares").add(lugar)
        .addOnSuccessListener { documentReference ->
            Log.d("Firestore", "Lugar creado correctamente con ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al crear lugar", e)
        }
}



