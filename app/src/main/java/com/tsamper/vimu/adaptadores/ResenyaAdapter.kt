package com.tsamper.vimu.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tsamper.vimu.R
import com.tsamper.vimu.VariablesGlobales
import com.tsamper.vimu.modelo.Opinion

class ResenyaAdapter (
    private var opiniones: List<Opinion>,
    private val context: Context
    ) : RecyclerView.Adapter<ResenyaAdapter.ResenyaViewHolder>() {

        class ResenyaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imagen: ImageView = view.findViewById(R.id.conciertoImageView)
            val nombre: TextView = view.findViewById(R.id.nombreConciertoTextView)
            val nombreUser: TextView = view.findViewById(R.id.nombreUsuarioText)
            val opinion: TextView = view.findViewById(R.id.opinionText)
            val recomendado: TextView = view.findViewById(R.id.recomendadoText)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResenyaViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_resenya_adapter, parent, false)
            return ResenyaViewHolder(view)
        }

        override fun onBindViewHolder(holder: ResenyaViewHolder, position: Int) {
            val opinion = opiniones[position]
            val imagenUrl = VariablesGlobales.conexion + "/" + opinion.concierto!!.imagen
            holder.nombre.text = opinion.concierto.nombre
            holder.nombreUser.text = opinion.usuario!!.nomUsuario
            holder.opinion.text = opinion.comentario
            holder.recomendado.text = opinion.recomendado.toString()
            Glide.with(context)
                .load(imagenUrl)
                .into(holder.imagen)
        }

        fun actualizarOpiniones(nuevaLista: List<Opinion>) {
            opiniones = nuevaLista
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int = opiniones.size
    }