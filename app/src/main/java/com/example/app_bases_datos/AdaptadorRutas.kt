package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorRutas(private val lugaresRutas: MutableList<RutaModelo>,
                     private val onRutaClick: (RutaModelo) -> Unit) :
RecyclerView.Adapter<AdaptadorRutas.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ruta_escogida, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.bind(lugaresRutas[position])
        val ruta = lugaresRutas[position]
        holder.bind(ruta)
        holder.itemView.setOnClickListener { onRutaClick(ruta) }

    }

    override fun getItemCount(): Int = lugaresRutas.size


    class MyViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val nombreR : TextView = itemView.findViewById(R.id.nombreRuta)
        val conteoR : TextView = itemView.findViewById(R.id.cantidadLugares)
        var imagenR : ImageView = itemView.findViewById(R.id.imagenMapa)

        @SuppressLint("SetTextI18n")
        fun bind(modelo: RutaModelo) {
            nombreR.text = modelo.nombre
            val conteoRutas = modelo.lugares.size
            conteoR.text = "$conteoRutas sitios."
            imagenR.setImageResource(R.drawable.mapa)
        }
    }
}