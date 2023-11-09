package com.jacgr.filmsrf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jacgr.filmsrf.R
import com.jacgr.filmsrf.databinding.ActivityMapBinding
import com.jacgr.filmsrf.util.Constants

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding

    //Para Google Maps
    private lateinit var map: GoogleMap

    private var studioName: String? = ""
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studioName = intent.getStringExtra(Constants.EXTRA_STUDIO_NAME)
        latitude = intent.getStringExtra(Constants.EXTRA_LATITUDE)?.toDouble()
        longitude = intent.getStringExtra(Constants.EXTRA_LONGITUDE)?.toDouble()

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
    }

    private fun createMarker() {
        val coordinates = LatLng(latitude!!, longitude!!)
        val marker = MarkerOptions()
            .position(coordinates)
            .title(studioName)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.popcorn))

        map.addMarker(marker)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )

    }

    override fun onRestart() {
        super.onRestart()
        if(!::map.isInitialized) return
    }

}