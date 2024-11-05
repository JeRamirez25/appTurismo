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

class LugarFavoritoAdapter(private val lugaresList: MutableList<FavoritoLugar>) : RecyclerView.Adapter<LugarFavoritoAdapter.LugarViewHolder>() {

    class LugarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.nombreLugaresFavoritos)
        val imagen: ImageView = view.findViewById(R.id.fotoLugarFavorito)
        val direccion: TextView = view.findViewById(R.id.direccionLugarFavorito)
        val tiempo: TextView = view.findViewById(R.id.tiempoLugarFavorito)
        val precio: TextView = view.findViewById(R.id.precioLugarFavorito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lugares_favoritos_usuario_item, parent, false)
        return LugarViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {

        val lugar = lugaresList[position]
        holder.nombre.text = lugar.nombre
        holder.direccion.text = lugar.direccion
        val horas = (lugar.tiempo / 60.0 * 10).toInt() / 10.0
        holder.tiempo.text = "$horas horas"
        holder.precio.text = lugar.precio.toString()
        Glide.with(holder.itemView.context).load(lugar.imagenURL).into(holder.imagen)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetallesLugarRuta::class.java)
            intent.putExtra("nombre", lugar.nombre)
            intent.putExtra("direccion", lugar.direccion)
            intent.putExtra("tiempo", "$horas horas")
            intent.putExtra("precio", lugar.precio)
            intent.putExtra("imagenURL", lugar.imagenURL)
            intent.putExtra("descripcion", lugar.descripcion)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = lugaresList.size
}