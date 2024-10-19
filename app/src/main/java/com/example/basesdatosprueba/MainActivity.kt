    package com.example.basesdatosprueba

    import android.content.ContentValues.TAG
    import android.os.Bundle
    import android.util.Log
    import android.widget.Toast
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import com.google.firebase.Firebase
    import com.google.firebase.firestore.firestore

    class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_main)

            val db = Firebase.firestore

            db.collection("lugares")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")

                        Toast.makeText(
                            this,
                            "ID: ${document.id}, Data: ${document.data}",
                            Toast.LENGTH_LONG
                        ).show()


                    }

                    Toast.makeText(
                        this,
                        "Terminó correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)

                    Toast.makeText(
                        this,
                        "se petateo",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
    }
