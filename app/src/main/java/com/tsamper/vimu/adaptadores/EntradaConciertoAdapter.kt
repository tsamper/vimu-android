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
import com.tsamper.vimu.modelo.EntradaConciertoGrouped

class EntradaConciertoAdapter(
    private val entradas: List<EntradaConciertoGrouped>,
    private val context: Context
) : RecyclerView.Adapter<EntradaConciertoAdapter.EntradaViewHolder>() {

    class EntradaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.conciertoImageView)
        val nombre: TextView = view.findViewById(R.id.nombreConciertoTextView)
        val fecha: TextView = view.findViewById(R.id.fechaConciertoTextView)
        val recinto: TextView = view.findViewById(R.id.recintoTextView)
        val cantidad: TextView = view.findViewById(R.id.cantidadEntradasTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntradaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entrada_concierto, parent, false)
        return EntradaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntradaViewHolder, position: Int) {
        val entradaGroup = entradas[position]
        val concierto = entradaGroup.concierto
        val imagenUrl = VariablesGlobales.conexion + "/" + concierto.imagen
        holder.nombre.text = concierto.nombre
        holder.fecha.text = "Fecha: ${concierto.fecha}"
        holder.recinto.text = "Recinto: ${concierto.recinto?.nombre}"
        holder.cantidad.text = "Entradas: ${entradaGroup.cantidad}"

        Glide.with(context)
            .load(imagenUrl)
            .into(holder.imagen)
    }

    override fun getItemCount(): Int = entradas.size
}
