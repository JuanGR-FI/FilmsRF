package com.jacgr.filmsrf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jacgr.filmsrf.data.remote.model.FilmDto
import com.jacgr.filmsrf.databinding.FilmElementBinding

class FilmsAdapter(
    private val films: List<FilmDto>,
    private val onFilmClicked: (FilmDto) -> Unit
): RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {

    class ViewHolder(private val binding: FilmElementBinding): RecyclerView.ViewHolder(binding.root) {
        val ivThumbnail = binding.ivThumbnail

        fun bind(film: FilmDto){
            binding.tvTitle.text = film.title
            binding.tvGenre.text = film.genre
            binding.tvYear.text = film.year
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FilmElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = films.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val film = films[position]

        holder.bind(film)

        Glide.with(holder.itemView.context)
            .load(film.thumbnail)
            .into(holder.ivThumbnail)

        holder.itemView.setOnClickListener {
            onFilmClicked(film)
        }

    }

}