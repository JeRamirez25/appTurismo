package com.example.app_bases_datos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RutaAdapter(
    private val rutas: MutableList<RutaModelo>,
    private val lugarId: String,
    private val usuarioId: String,
    private val onRutaCheckedChange: (RutaModelo, Boolean) -> Unit
) : RecyclerView.Adapter<RutaAdapter.RutaViewHolder>() {

    inner class RutaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreRuta: TextView = itemView.findViewById(R.id.tvNombreRuta)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxRuta)

        fun bind(ruta: RutaModelo) {
            nombreRuta.text = ruta.nombre
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                onRutaCheckedChange(ruta, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rutas_guardar_favoritos, parent, false)
        return RutaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RutaViewHolder, position: Int) {
        holder.bind(rutas[position])
    }

    override fun getItemCount() = rutas.size
}