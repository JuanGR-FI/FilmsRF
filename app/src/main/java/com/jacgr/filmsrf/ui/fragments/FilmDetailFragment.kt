package com.jacgr.filmsrf.ui.fragments

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.jacgr.filmsrf.R
import com.jacgr.filmsrf.application.FilmRFApp
import com.jacgr.filmsrf.data.FilmRepository
import com.jacgr.filmsrf.data.remote.model.FilmDetailDto
import com.jacgr.filmsrf.databinding.FragmentFilmDetailBinding
import com.jacgr.filmsrf.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val FILM_ID = "film_id"
class FilmDetailFragment : Fragment() {

    private var _binding: FragmentFilmDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: FilmRepository

    private var filmId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {args ->
            filmId = args.getString(FILM_ID)

            //Log.d(Constants.LOGTAG, "Id recibido: $filmId")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFilmDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as FilmRFApp).repository

        binding.btnReload.setOnClickListener {
            binding.pbLoading.visibility = View.VISIBLE
            binding.llConnectionError.visibility = View.GONE
            tryConnection()
        }

        tryConnection()

    }

    private fun tryConnection(){
        lifecycleScope.launch {
            filmId?.let {id ->
                val call: Call<FilmDetailDto> = repository.getFilmDetail(id)

                call.enqueue(object: Callback<FilmDetailDto> {
                    override fun onResponse(
                        call: Call<FilmDetailDto>,
                        response: Response<FilmDetailDto>
                    ) {

                        //Log.d(Constants.LOGTAG, "Respuesta detail: : ${response.body()}")

                        binding.apply {
                            pbLoading.visibility = View.GONE

                            clContent.visibility = View.VISIBLE

                            tvTitle.text = response.body()?.title
                            tvGenre.text = response.body()?.genre
                            tvDirector.text = getString(R.string.directed_by, response.body()?.director)
                            tvYear.text = response.body()?.year
                            tvStars.text = response.body()?.stars
                            tvOverview.text = response.body()?.overview

                            vvVideo.setVideoURI(Uri.parse(response.body()?.video))
                            binding.vvVideo.start()

                            Glide.with(requireContext())
                                .load(response.body()?.image)
                                .into(ivImage)
                        }

                    }

                    override fun onFailure(call: Call<FilmDetailDto>, t: Throwable) {
                        binding.pbLoading.visibility = View.GONE
                        binding.llConnectionError.visibility = View.VISIBLE

                        //Toast.makeText(requireActivity(), "No hay conexion", Toast.LENGTH_SHORT).show()
                    }

                })

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(filmId: String) =
            FilmDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(FILM_ID, filmId)
                }
            }
    }

}