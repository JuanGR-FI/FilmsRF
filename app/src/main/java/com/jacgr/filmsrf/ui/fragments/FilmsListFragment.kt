package com.jacgr.filmsrf.ui.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.jacgr.filmsrf.R
import com.jacgr.filmsrf.application.FilmRFApp
import com.jacgr.filmsrf.data.FilmRepository
import com.jacgr.filmsrf.data.remote.model.FilmDto
import com.jacgr.filmsrf.databinding.FragmentFilmsListBinding
import com.jacgr.filmsrf.ui.Login
import com.jacgr.filmsrf.ui.adapters.FilmsAdapter
import com.jacgr.filmsrf.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmsListFragment : Fragment() {

    private var _binding: FragmentFilmsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: FilmRepository

    private lateinit var mp: MediaPlayer

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFilmsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userEmail = firebaseAuth.currentUser?.email

        binding.tvUserEmail.text = userEmail

        repository = (requireActivity().application as FilmRFApp).repository

        binding.btnReload.setOnClickListener {
            binding.pbLoading.visibility = View.VISIBLE
            binding.llConnectionError.visibility = View.GONE
            tryConnection()
        }

        binding.btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            activity?.startActivity(Intent(requireContext(), Login::class.java))
            activity?.finish()
        }

        tryConnection()
    }

    private fun tryConnection() {
        lifecycleScope.launch {
            val call: Call<List<FilmDto>> = repository.getFilms()

            call.enqueue(object: Callback<List<FilmDto>>{
                override fun onResponse(
                    call: Call<List<FilmDto>>,
                    response: Response<List<FilmDto>>
                ) {
                    binding.pbLoading.visibility = View.GONE
                    //Log.d(Constants.LOGTAG, "Respuesta del servidor: ${response.body()}")

                    response.body()?.let { films ->
                        binding.rvFilms.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = FilmsAdapter(films){film ->
                                film.id?.let {id ->
                                    mp = MediaPlayer.create(requireContext(), R.raw.pop)
                                    mp.start()
                                    requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, FilmDetailFragment.newInstance(id))
                                        .addToBackStack(null)
                                        .commit()
                                }
                            }
                        }
                    }


                }

                override fun onFailure(call: Call<List<FilmDto>>, t: Throwable) {
                    //Log.d(Constants.LOGTAG, "Error: ${t.message}")
                    //Toast.makeText(requireContext(), "No hay conexion", Toast.LENGTH_SHORT).show()

                    binding.pbLoading.visibility = View.GONE


                    binding.llConnectionError.visibility = View.VISIBLE
                }

            })


        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}