package com.tsamper.vimu.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tsamper.vimu.R
import com.tsamper.vimu.VariablesGlobales
import com.tsamper.vimu.modelo.Cancion

class CancionAdapter  (
    private var canciones: List<Cancion>,
    private val context: Context
) : RecyclerView.Adapter<CancionAdapter.CancionViewHolder>() {

    class CancionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.tituloCancionTextView)
        val video: WebView = view.findViewById(R.id.youtubeWebView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cancion_adapter, parent, false)
        return CancionViewHolder(view)
    }

    override fun onBindViewHolder(holder: CancionViewHolder, position: Int) {
        val cancion = canciones[position]
        holder.titulo.text = cancion.titulo
        val videoId = cancion.enlaceYoutube
        val html = """
            <iframe width="100%" height="100%" 
                src="https://www.youtube.com/embed/$videoId" 
                frameborder="0" allowfullscreen>
            </iframe>
            """.trimIndent()

        holder.video.settings.javaScriptEnabled = true
        holder.video.loadData(html, "text/html", "utf-8")
    }

    fun actualizarCanciones(nuevaLista: List<Cancion>) {
        canciones = nuevaLista
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = canciones.size
}
