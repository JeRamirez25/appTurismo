package com.example.app_bases_datos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.app_bases_datos.utils.nombreUsuario

class Perfil : Fragment() {
    private val auth: FirebaseAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_perfil, container, false)

        val logoutBtn = root.findViewById<Button>(R.id.logout)
        val info = root.findViewById<TextView>(R.id.texto)

        val user = auth.currentUser
        if (user != null) {
            val userEmail = user.email ?: ""
            nombreUsuario(userEmail, info)
        } else {
            info.text = "Usuario no autenticado"
        }

        logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, Login::class.java)
            startActivity(intent)
        }
        return root
    }

}