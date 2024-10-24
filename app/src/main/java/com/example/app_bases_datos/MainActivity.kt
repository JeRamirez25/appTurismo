package com.example.app_bases_datos

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.app_bases_datos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var emailTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        emailTextView = binding.emailTextView
        val email = intent.getStringExtra("EMAIL")
        Log.d("MainActivity", "Correo recibido: $email")
        emailTextView.text = if (email != null) "Hola, $email" else "No se proporcionó correo"


        replaceFragment(Dos_mitades())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.dos_mitades -> replaceFragment(Dos_mitades())
                R.id.dos_palabras -> replaceFragment(Dos_palabras())
                R.id.quitar_fragmento -> replaceFragment(Quitar_fragmento())
            }
            true
        }
    }
    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}