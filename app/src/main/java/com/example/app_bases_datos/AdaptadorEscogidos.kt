package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdaptadorEscogidos(private val lugaresEscogidos: MutableList<Lugar>) :
    RecyclerView.Adapter<AdaptadorEscogidos.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lugar_escogido, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(lugaresEscogidos[position])
    }

    override fun getItemCount(): Int {
        return lugaresEscogidos.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imagenE : ImageView = itemView.findViewById(R.id.imagenEscogida)
        val nombreE : TextView = itemView.findViewById(R.id.nombreEscogido)
        val direccionE : TextView = itemView.findViewById(R.id.direccionEscogido)
        val tiempoE : TextView = itemView.findViewById(R.id.tiempoEscogido)

        @SuppressLint("SetTextI18n")
        fun bind(lugar: Lugar) {
            nombreE.text = lugar.nombre
            direccionE.text = lugar.direccion
            val horas = (lugar.tiempo / 60.0 * 10).toInt() / 10.0
            tiempoE.text = "$horas horas"
            Glide.with(itemView.context).load(lugar.imagenURL).into(imagenE)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetallesLugarRuta::class.java)
                intent.putExtra("nombre", lugar.nombre)
                intent.putExtra("direccion", lugar.direccion)
                intent.putExtra("descripcion", lugar.descripcion)
                intent.putExtra("precio", lugar.precio)
                intent.putExtra("tiempo", lugar.tiempo)
                intent.putExtra("imagenURL", lugar.imagenURL)
                itemView.context.startActivity(intent)
            }
        }
    }
}