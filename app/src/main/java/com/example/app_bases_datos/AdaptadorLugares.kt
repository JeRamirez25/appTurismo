package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class LugarAdapter(private val lugares: List<Lugar>, private val onItemClick: (Lugar) -> Unit
) : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lugar, parent, false)
        return LugarViewHolder(view)
    }

    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        holder.bind(lugares[position])

    }

    override fun getItemCount(): Int = lugares.size

    inner class LugarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivLugar: ImageView = itemView.findViewById(R.id.ivLugar)
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreLugar)
        private val tvDireccion: TextView = itemView.findViewById(R.id.tvDireccionLugar)
        private val tvTiempo: TextView = itemView.findViewById(R.id.tvTiempo)

        @SuppressLint("SetTextI18n")
        fun bind(lugar: Lugar) {
            tvNombre.text = lugar.nombre
            tvDireccion.text = lugar.direccion
            val horas = (lugar.tiempo / 60.0 * 10).toInt() / 10.0
            tvTiempo.text = "$horas horas"
            Glide.with(itemView.context).load(lugar.imagenURL).into(ivLugar)

            itemView.setOnClickListener { onItemClick(lugar) }


        }
    }
}