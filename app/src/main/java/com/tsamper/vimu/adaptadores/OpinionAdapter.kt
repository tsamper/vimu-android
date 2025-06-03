package com.tsamper.vimu.adaptadores

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
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
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tsamper.vimu.R
import com.tsamper.vimu.VariablesGlobales
import com.tsamper.vimu.conexion.RetrofitClient
import com.tsamper.vimu.modelo.Concierto
import com.tsamper.vimu.modelo.Usuario
import com.tsamper.vimu.modelo.enums.OpcionesOpinion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpinionAdapter (
    private var conciertos: List<Concierto>,
    private var idUser: Int,
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
            val apiService = RetrofitClient.getApiService()
            holder.nombre.text = concierto.nombre
            holder.fecha.text = "Fecha: ${concierto.fecha}"
            holder.recinto.text = "Recinto: ${concierto.recinto?.nombre}"
            apiService.comprobarComentarioPorUsuarioYConcierto(concierto.id, idUser).enqueue(object : Callback<Boolean> {
                @SuppressLint("NotifyDataSetChanged")
                @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.d("API", "Accediendo llamada a la API")
                    if (response.isSuccessful) {
                        if (response.body() != true){
                            holder.botonOpinion.setOnClickListener {
                                val dialogView = LayoutInflater.from(context)
                                    .inflate(R.layout.dialog_opinion, null)
                                val editOpinion =
                                    dialogView.findViewById<EditText>(R.id.editOpinion)
                                val spinner =
                                    dialogView.findViewById<Spinner>(R.id.spinnerRecomendado)

                                val opciones = listOf("Recomendado", "No recomendado")
                                val adapter = ArrayAdapter(
                                    context,
                                    android.R.layout.simple_spinner_item,
                                    opciones
                                )
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spinner.adapter = adapter

                                AlertDialog.Builder(context)
                                    .setTitle("Opinión sobre ${concierto.nombre}")
                                    .setView(dialogView)
                                    .setPositiveButton("Enviar") { dialog, _ ->
                                        val opinionTexto = editOpinion.text.toString()
                                        val recomendado = spinner.selectedItem.toString()
                                        var reco: OpcionesOpinion = OpcionesOpinion.RECOMENDADO
                                        if (recomendado.equals(OpcionesOpinion.NO_RECOMENDADO.toString())) {
                                            reco = OpcionesOpinion.NO_RECOMENDADO
                                        }
                                        apiService.registrarComentario(
                                            concierto.id,
                                            idUser,
                                            reco,
                                            opinionTexto
                                        ).enqueue(object : Callback<Void> {
                                            @SuppressLint("NotifyDataSetChanged")
                                            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
                                            override fun onResponse(
                                                call: Call<Void>,
                                                response: Response<Void>
                                            ) {
                                                Log.d("API", "Accediendo llamada a la API")
                                                if (response.isSuccessful) {

                                                } else {
                                                    Log.d("API", "ERROR: " + response.message())
                                                }
                                            }

                                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                                Log.d("API", "Fallo llamada a la API: " + t.message)
                                            }
                                        })

                                        dialog.dismiss()
                                    }
                                    .setNegativeButton("Cancelar") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .show()
                            }
                        }else{
                            holder.botonOpinion.isEnabled = false
                            holder.botonOpinion.alpha = 0.5f
                        }
                    } else {
                        Log.d("API", "ERROR: " + response.message())
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.d("API", "Fallo llamada a la API: " + t.message)
                }
            })


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
