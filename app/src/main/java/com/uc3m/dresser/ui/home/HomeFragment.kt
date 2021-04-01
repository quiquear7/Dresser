package com.uc3m.dresser.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentHomeBinding
import com.uc3m.dresser.repository.Repository
import com.uc3m.dresser.viewModels.MainViewModel
import com.uc3m.dresser.viewModels.MainViewModelFactory
import com.uc3m.dresser.viewModels.PrendaViewModel



class HomeFragment : Fragment() {
    private var temperatura: Float? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var prendaViewModel: PrendaViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)


        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        if (context?.checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && context?.checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permisosCamara = arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissions(permisosCamara, 1)
        }else{
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latitud = location.latitude.toString()
                        val longitud = location.longitude.toString()
                        llamarApi(latitud, longitud, binding)
                    }
                }
        }

        prendaViewModel = ViewModelProvider(this).get(PrendaViewModel::class.java)
        prendaViewModel.lastOutfit.observe(viewLifecycleOwner, { prendas ->
            if (prendas != null) {
                val i = prendas.prenda
                if (i.parteSuperior != null) {
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(i.parteSuperior!!.ruta)
                    binding.iButton1.setImageBitmap(imgBitmap)
                }
                if (i.parteInferior != null) {
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(i.parteInferior!!.ruta)
                    binding.iButton2.setImageBitmap(imgBitmap)
                }
                if (i.calzado != null) {
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(i.calzado!!.ruta)
                    binding.iButton3.setImageBitmap(imgBitmap)
                }
                if (i.cazadoras != null) {
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(i.cazadoras!!.ruta)
                    binding.iButton4.setImageBitmap(imgBitmap)
                }
                if (i.jerseis != null) {
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(i.jerseis!!.ruta)
                    binding.iButton5.setImageBitmap(imgBitmap)
                }
                if (i.conjuntos != null) {
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(i.conjuntos!!.ruta)
                    binding.iButton6.setImageBitmap(imgBitmap)
                }
            }
        })

        binding.fabHome.setOnClickListener{
            if(temperatura!=null){
                setFragmentResult("envioTemp", bundleOf("temperatura" to temperatura))
                findNavController().navigate(R.id.action_navigation_home_to_formularioFragment)
            }
            else{
                Toast.makeText(
                    requireActivity(),
                    "No se ha obtenido Temperatura",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
            if (response.isSuccessful) {
                val w = response.body()
                if (w != null) {
                    val imgclima = w.weather[0].icon

                    Picasso.with(context).load("https://openweathermap.org/img/wn/$imgclima@2x.png")
                        .into(binding.imageTiempo)

                    temperatura = w.main.temp
                    val tempInt = temperatura!!.toInt()
                    binding.textCity.text = w.name +", "+w.sys.country
                    binding.tTemp.text = tempInt.toString() + " °C"
                    val sTermica = w.main.feels_like.toInt()
                    binding.sTermica.text = "Sensación Termica: "+sTermica.toString()+ " °C"
                   /* val min = w.main.temp_min.toInt()
                    val max = w.main.temp_max.toInt()
                    binding.textMaxMin.text = "Max: "+max.toString()+" °C  \n"+"Min:  "+min.toString()+" °C"*/
                }
            }
        })
    }



}