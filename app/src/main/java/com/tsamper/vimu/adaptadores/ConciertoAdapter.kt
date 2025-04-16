package com.tsamper.vimu.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tsamper.vimu.R
import com.tsamper.vimu.modelo.Concierto

class ConciertoAdapter(
    private val conciertoList: List<Concierto>,
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

        val baseUrl = "http://192.168.4.138:8080/" // Cambialo por tu IP local + puerto
        val imagenUrl = baseUrl + item.imagen // ejemplo: "img/carteles/Hoke_Murcia.jpg"

        Glide.with(holder.itemView.context)
            .load(imagenUrl)
            //.placeholder(R.drawable.placeholder) // una imagen temporal mientras carga
            //.error(R.drawable.error_image)       // por si falla la carga
            .into(holder.imagen)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = conciertoList.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreText: TextView = itemView.findViewById(R.id.nombreText)
        val fechaText: TextView = itemView.findViewById(R.id.fechaText)
        val imagen: ImageView = itemView.findViewById(R.id.conciertoImage)
    }
}