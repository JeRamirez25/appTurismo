package com.example.app_bases_datos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class LugarDetalle : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.lugar_detalles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivFotoLugarDetalles: ImageView = view.findViewById(R.id.ivFotoLugar)
        val tvNombreLugarDetalles: TextView = view.findViewById(R.id.tvNombreLugar)
        val tvPrecioLugarDetalles: TextView = view.findViewById(R.id.tvPrecioLugar)
        val tvDescripcionLugarDetalles: TextView = view.findViewById(R.id.tvDescripcionLugar)
        val tvDireccionLugarDetalles: TextView = view.findViewById(R.id.tvDireccionLugar)
        val tvTiempoLugar: TextView = view.findViewById(R.id.tvTiempoLugar)

        arguments?.let { bundle ->
            tvNombreLugarDetalles.text = bundle.getString("nombre")
            tvPrecioLugarDetalles.text = bundle.getString("precio")
            tvDescripcionLugarDetalles.text = bundle.getString("descripcion")
            tvDireccionLugarDetalles.text = bundle.getString("direccion")
            tvTiempoLugar.text = bundle.getString("tiempo")

            val imagenUrl = bundle.getString("imagenURL")
            Glide.with(this).load(imagenUrl).into(ivFotoLugarDetalles)
        }

        view.findViewById<ImageView>(R.id.boton_cerrar).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}