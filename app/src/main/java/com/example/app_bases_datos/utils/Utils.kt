package com.example.app_bases_datos.utils

import android.util.Log
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// Esta función permite crear usuarios
// ENTRADA: nombre, correo

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

// Esta función permite añadir Lugares a la BD
// ENTRADA: nombre, categoria, descripcion, direccion, imagen, precio, tiempo
// ATENCION: La base de datos obtiene otro parametro: GEOPOINT, este debe añadirse manualmente

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

// --------------------------------------------------- LUGARES FAVORITOS ------------------------------------------------------------

fun añadirLugar(idUsuario: String, idLugar: String){
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection("usuarios").document(idUsuario)

    usuarioRef.update("lugaresFavoritos", FieldValue.arrayUnion(idLugar))
        .addOnSuccessListener {
            Log.d("Firestore", "Lugar con ID: $idLugar añadido a los lugares favoritos del usuario $idUsuario")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al añadir lugar a los lugares favoritos del usuario $idUsuario", e)
        }
}

fun eliminarLugar(idUsuario: String, idLugar: String){
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection("usuarios").document(idUsuario)

    usuarioRef.update("lugaresFavoritos", FieldValue.arrayRemove(idLugar))
        .addOnSuccessListener {
            Log.d("Firestore", "Lugar con ID: $idLugar eliminado de los lugares favoritos del usuario $idUsuario")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al eliminar lugar de los lugares favoritos del usuario $idUsuario", e)
        }
}

// ------------------------------------------------- RUTAS ----------------------------------------------------------

// Esta función AÑADE rutas a usuarios y las crea en la base de datos
// Entrada: ID_USUARIO, ID_RUTA & NOMBRE

fun crearRuta(idUsuario: String, nombreRuta: String) {
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection("usuarios").document(idUsuario)

    // Crea una nueva ruta
    val nuevaRuta = hashMapOf(
        "nombre" to nombreRuta, // Nombre de la ruta o cualquier otro dato relevante
        "lugares" to listOf<String>(), // Lista vacía de lugares por ahora
    )

    // Agregar la nueva ruta a la colección "rutas"
    val rutaRef = db.collection("rutas").add(nuevaRuta)
        .addOnSuccessListener { documentReference ->
            val rutaId = documentReference.id
            Log.d("Firestore", "Ruta $rutaId creada y agregada a la base de datos de rutas")

            // Ahora añade la ruta a las rutas favoritas del usuario
            usuarioRef.update("rutasFavoritas", FieldValue.arrayUnion(rutaId))
                .addOnSuccessListener {
                    Log.d("Firestore", "Ruta $rutaId agregada a las rutas favoritas del usuario $idUsuario")
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error al agregar ruta a las favoritas del usuario", e)
                }
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al crear la ruta en la base de datos", e)
        }
}

// Esta función ELIMINA rutas a usuarios y las elimina en la base de datos
// Entrada: ID USUARIO & ID RUTA

fun eliminarRuta(idUsuario: String, idRuta: String) {
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection("usuarios").document(idUsuario)
    val rutaRef = db.collection("rutas").document(idRuta)

    // Eliminar la ruta de las rutas favoritas del usuario
    usuarioRef.update("rutasFavoritas", FieldValue.arrayRemove(idRuta))
        .addOnSuccessListener {
            Log.d("Firestore", "La ruta $idRuta ha sido eliminada de las rutas favoritas del usuario $idUsuario")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al eliminar ruta de las favoritas del usuario", e)
        }

    // Eliminar la ruta de la colección de rutas
    rutaRef.delete()
        .addOnSuccessListener {
            Log.d("Firestore", "La ruta $idRuta ha sido eliminada de la base de datos de rutas")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al eliminar la ruta $idRuta de la base de datos de rutas", e)
        }
}

// Esta función verifica si una ruta existe
// ENTRADA: ID_LUGAR
// SALIDA: BOOLEAN

suspend fun verificarRuta(idUsuario: String): Boolean {
    return try {
        val db = FirebaseFirestore.getInstance()
        val usuarioRef = db.collection("usuarios").document(idUsuario)
        val document = usuarioRef.get().await()
        if (document.exists()) {
            val rutasFavoritas = document.get("rutasFavoritas") as? List<*>
            return rutasFavoritas?.isNotEmpty() == true
        } else {
            return false
        }
    } catch (e: Exception) {
        Log.w("Firestore", "Error al verificar las rutas del usuario", e)
        return false
    }
}

// ------------------------------------------------- LUGARES A RUTAS ----------------------------------------------------------

// Esta función AÑADE lugares en las rutas de usuarios
// ENTRADA: ID_RUTA & ID_LUGAR

fun añadirLugarRuta(idRuta: String, idLugar: String) {
    val db = Firebase.firestore
    val rutaRef = db.collection("rutas").document(idRuta)

    // Añadir el lugar a la lista de lugares de la ruta
    val rutaUpdate = rutaRef.update("lugares", FieldValue.arrayUnion(idLugar))
    rutaUpdate.addOnSuccessListener {
        Log.d("Firestore", "Lugar $idLugar añadido a la ruta $idRuta")
    }.addOnFailureListener { e ->
        Log.w("Firestore", "Error al añadir lugar a la ruta $idRuta", e)
    }
}

// Esta función ELIMINA lugares en las rutas de usuarios
// ENTRADA: ID_USUARIO, ID_RUTA, ID_LUGAR

fun eliminarLugarRuta(idRuta: String, idLugar: String) {
    val db = Firebase.firestore
    val rutaRef = db.collection("rutas").document(idRuta)

    // Eliminar la ruta de la base de datos
    val rutaUpdate = rutaRef.update("lugares", FieldValue.arrayRemove(idLugar))
    rutaUpdate.addOnSuccessListener {
        Log.d("Firestore", "Lugar ${idLugar} eliminado de la ruta $idRuta")
    }.addOnFailureListener { e ->
        Log.w("Firestore", "Error al eliminar lugar de la ruta $idRuta", e)
    }
}

// ------------------------------------------------- BUSQUEDAS EN LA BD ------------------------------------------------------

// Esta función permite obtener el ID del usuario a partir del correo
// ENTRADA: CORREO_USUARIO

fun obtenerIdUsuario(correo: String, onComplete: (String?) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("usuarios")
        .whereEqualTo("correo", correo) // Asegúrate de que el campo se llama "correo" en tu Firestore
        .get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                // Asumiendo que el ID del documento es el ID del usuario
                val usuarioId = documents.documents[0].id
                onComplete(usuarioId) // Devuelve el ID del usuario
            } else {
                onComplete(null) // No se encontró el usuario
            }
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al buscar el usuario por correo", e)
            onComplete(null) // Maneja el caso de error
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

// Esta función muestra el nombre del usuario a partir del correo
// ENTRADA: CORREO

fun nombreUsuario(email: String, infoTextView: TextView) {
    val db = FirebaseFirestore.getInstance()

    db.collection("usuarios")
        .whereEqualTo("id", email)
        .get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val nombre = documents.documents[0].getString("nombre")
                infoTextView.text = nombre // Muestra el nombre en el TextView
            } else {
                infoTextView.text = "Nombre no encontrado"
            }
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al obtener el nombre del usuario", e)
            infoTextView.text = "Error al cargar el nombre"
        }
}

// Esta función muestra el nombre del usuario a partir del correo y lo saluda
// ENTRADA: CORREO

fun saludo(email: String, infoTextView: TextView) {
    val db = FirebaseFirestore.getInstance()

    db.collection("usuarios")
        .whereEqualTo("id", email)
        .get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val nombre = documents.documents[0].getString("nombre")
                infoTextView.text = "Hola, "+nombre // Muestra el nombre en el TextView
            } else {
                infoTextView.text = "Desconocido"
            }
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al obtener el nombre del usuario", e)
            infoTextView.text = "Error al cargar el nombre"
        }
}
fun verificarLugarFavorito(idUsuario: String, idLugar: String, onComplete: (Boolean) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection("usuarios").document(idUsuario)

    usuarioRef.get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val lugaresFavoritos = document.get("lugaresFavoritos") as? List<String>
                // Verificamos si el lugar especificado está en la lista de lugares favoritos
                val esFavorito = lugaresFavoritos?.contains(idLugar) == true
                onComplete(esFavorito)
            } else {
                // El documento del usuario no existe
                onComplete(false)
            }
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al verificar lugar favorito del usuario", e)
            onComplete(false) // En caso de error, devolvemos false
        }
}

