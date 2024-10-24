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


class Quitar_fragemento : Fragment() {

    private val auth: FirebaseAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_perfil, container, false)

        val logoutBtn = root.findViewById<Button>(R.id.logout)
        val user = auth.currentUser
        val info = root.findViewById<TextView>(R.id.texto)

        info.text = user!!.email

        logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, Start::class.java)
            startActivity(intent)

        }

        return root
    }

}