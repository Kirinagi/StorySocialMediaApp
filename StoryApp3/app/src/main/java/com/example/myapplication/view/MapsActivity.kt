package com.example.myapplication.view

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.example.dicodingstoryapp1.R
import com.example.dicodingstoryapp1.databinding.ActivityMapsBinding
import com.example.myapplication.UserPreference
import com.example.myapplication.ViewModelFactory
import com.example.myapplication.view.story.StoryViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
@ExperimentalPagingApi
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var storyMapViewModel: StoryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(getStoriesLocationCallback)

        setupViewModel()

    }


    private fun setupViewModel() {
        storyMapViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[StoryViewModel::class.java]
    }


    private val getStoriesLocationCallback = OnMapReadyCallback{ googleMap ->
        lifecycleScope.launch {
            storyMapViewModel.getData().forEach {
                val storyLocation = LatLng(it.lat, it.lon)
                googleMap.uiSettings.isZoomControlsEnabled = true
                try {
                    val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(applicationContext, R.raw.map_style))
                    if (!success){
                        Log.e("TAG", "Parsing Failed")
                    }
                }catch (exception: Resources.NotFoundException){
                    Log.e("TAG", "Map Style not found", exception)
                }
                googleMap.addMarker(MarkerOptions().position(storyLocation).title(it.name).snippet(it.description).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(storyLocation))
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                storyMapViewModel.logout()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }


    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }
}