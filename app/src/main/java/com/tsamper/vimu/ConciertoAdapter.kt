package com.tsamper.vimu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tsamper.vimu.modelo.Concierto

class ConciertoAdapter(
    private val itemList: List<Concierto>,
    private val onItemClick: (Concierto) -> Unit
) : RecyclerView.Adapter<ConciertoAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_concierto_adapter, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.codigoText.text = "Servicio: "
        holder.codVehiculoText.text = "Matrícula: "


        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = itemList.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val codigoText: TextView = itemView.findViewById(R.id.codigoText)
        val codVehiculoText: TextView = itemView.findViewById(R.id.codVehiculoText)
    }
}