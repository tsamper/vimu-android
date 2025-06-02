package com.tsamper.vimu.adaptadores

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tsamper.vimu.R
import com.tsamper.vimu.VariablesGlobales
import com.tsamper.vimu.modelo.Concierto

class OpinionAdapter (
    private var conciertos: List<Concierto>,
    private val context: Context
    ) : RecyclerView.Adapter<OpinionAdapter.OpinionViewHolder>() {

        class OpinionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imagen: ImageView = view.findViewById(R.id.conciertoImageView)
            val nombre: TextView = view.findViewById(R.id.nombreConciertoTextView)
            val fecha: TextView = view.findViewById(R.id.fechaConciertoTextView)
            val recinto: TextView = view.findViewById(R.id.recintoTextView)
            val botonOpinion: Button = view.findViewById(R.id.opinionBtn)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpinionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_opinion_adapter, parent, false)
            return OpinionViewHolder(view)
        }

        override fun onBindViewHolder(holder: OpinionViewHolder, position: Int) {
            val concierto = conciertos[position]
            val imagenUrl = VariablesGlobales.conexion + "/" + concierto.imagen
            holder.nombre.text = concierto.nombre
            holder.fecha.text = "Fecha: ${concierto.fecha}"
            holder.recinto.text = "Recinto: ${concierto.recinto?.nombre}"
            holder.botonOpinion.setOnClickListener {
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_opinion, null)
                val editOpinion = dialogView.findViewById<EditText>(R.id.editOpinion)
                val spinner = dialogView.findViewById<Spinner>(R.id.spinnerRecomendado)

                val opciones = listOf("Recomendado", "No recomendado")
                val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, opciones)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter

                AlertDialog.Builder(context)
                    .setTitle("Opinión sobre ${concierto.nombre}")
                    .setView(dialogView)
                    .setPositiveButton("Enviar") { dialog, _ ->
                        val opinionTexto = editOpinion.text.toString()
                        val recomendado = spinner.selectedItem.toString()
                        // Aquí puedes enviar la opinión al backend o hacer algo con los datos

                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
            Glide.with(context)
                .load(imagenUrl)
                .into(holder.imagen)
        }

    fun actualizarConciertos(nuevaLista: List<Concierto>) {
        conciertos = nuevaLista
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = conciertos.size
    }
