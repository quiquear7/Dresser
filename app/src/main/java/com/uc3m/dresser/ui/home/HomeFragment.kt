package com.uc3m.dresser.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentHomeBinding
import com.uc3m.dresser.repository.Repository
import com.uc3m.dresser.viewModels.MainViewModel
import com.uc3m.dresser.viewModels.MainViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        if (context?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && context?.checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permisosCamara = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            requestPermissions(permisosCamara,1)
        }else{
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if (location != null) {
                        val latitud = location.latitude.toString()
                        val longitud = location.longitude.toString()
                        llamarApi(latitud, longitud, binding)
                    }
                }
        }

        binding.fabHome.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_home_to_formularioFragment)
        }





        return view
    }

    private fun llamarApi(latitud: String, longitud: String, binding: FragmentHomeBinding) {
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        val map = mapOf("lat" to latitud, "lon" to longitud)
        viewModel.getClima(map)
        viewModel.myResponse.observe(viewLifecycleOwner, { response ->
            if(response.isSuccessful){
                val w = response.body()?.main?.temp
                val temperatura =  w.toString()
                Log.i("Temp", temperatura)
                val texto = binding.tTemp
                texto.text = "Temperatura Actual: "+temperatura +"ºC"
            }
        })
    }



}