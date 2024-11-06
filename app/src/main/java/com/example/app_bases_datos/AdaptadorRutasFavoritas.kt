package com.example.app_bases_datos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RutaAdapter(
private val listaRutas: List<RutaModelo>,
private val lugarId: String,
private val usuarioId: String,
private val onCheckChanged: (RutaModelo, Boolean) -> Unit
) : RecyclerView.Adapter<RutaAdapter.RutaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rutas_guardar_favoritos, parent, false)
        return RutaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RutaViewHolder, position: Int) {
        val ruta = listaRutas[position]
        holder.bind(ruta)
    }

    override fun getItemCount(): Int = listaRutas.size

    inner class RutaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxRuta)  // CheckBox para marcar la ruta
        private val textView: TextView = itemView.findViewById(R.id.tvNombreRuta)  // Nombre de la ruta

        fun bind(ruta: RutaModelo) {
            textView.text = ruta.nombre
            checkBox.isChecked = ruta.lugares.contains(lugarId)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                onCheckChanged(ruta, isChecked)
            }
        }
    }
}
