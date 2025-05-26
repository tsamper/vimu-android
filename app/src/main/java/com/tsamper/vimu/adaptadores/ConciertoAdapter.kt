package com.tsamper.vimu.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tsamper.vimu.R

import com.tsamper.vimu.VariablesGlobales
import com.tsamper.vimu.modelo.Concierto

class ConciertoAdapter(
    private val conciertoList: MutableList<Concierto>,
    private val onItemClick: (Concierto) -> Unit
) : RecyclerView.Adapter<ConciertoAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_concierto_adapter, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = conciertoList[position]
        holder.nombreText.text = item.nombre
        holder.fechaText.text = item.fecha

        val imagenUrl = VariablesGlobales.conexion + "/" + item.imagen

        Glide.with(holder.itemView.context)
            .load(imagenUrl)
            //.placeholder(R.drawable.placeholder)
            //.error(R.drawable.error_image)
            .into(holder.imagen)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = conciertoList.size

    fun actualizarConciertos(nuevos: List<Concierto>) {
        conciertoList.clear()
        conciertoList.addAll(nuevos)
        notifyDataSetChanged()
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreText: TextView = itemView.findViewById(R.id.nombreText)
        val fechaText: TextView = itemView.findViewById(R.id.fechaText)
        val imagen: ImageView = itemView.findViewById(R.id.conciertoImage)
    }
}