package com.uc3m.dresser.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import com.uc3m.dresser.MainActivity
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentHomeBinding
import com.uc3m.dresser.repository.Repository
import com.uc3m.dresser.viewModels.MainViewModel
import com.uc3m.dresser.viewModels.MainViewModelFactory
import com.uc3m.dresser.viewModels.PrendaViewModel


class HomeFragment : Fragment() {
    private var temperatura: Float? = null
    private var llueve: Boolean = false
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
        binding.srl.setColorSchemeColors(resources.getColor(R.color.cyan))
        coordenadas()




        prendaViewModel = ViewModelProvider(this).get(PrendaViewModel::class.java)
        prendaViewModel.lastOutfit.observe(viewLifecycleOwner, { registro ->
            if (registro != null) {
                binding.tituloOutfit.text = "ÚLTIMO OUTFIT"
                val i = registro.prenda
                if (i.parteSuperior != null) {
                    val iv: ByteArray = Base64.decode(i.parteSuperior!!.iv, Base64.DEFAULT)
                    val text: ByteArray = Base64.decode(
                        i.parteSuperior!!.encryptedRuta,
                        Base64.DEFAULT
                    )
                    val ruta = homeViewModel.decryptData(iv, text)
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(ruta)
                    binding.iButton1.setImageBitmap(imgBitmap)
                }
                if (i.parteInferior != null) {
                    val iv: ByteArray = Base64.decode(i.parteInferior!!.iv, Base64.DEFAULT)
                    val text: ByteArray = Base64.decode(
                        i.parteInferior!!.encryptedRuta,
                        Base64.DEFAULT
                    )
                    val ruta = homeViewModel.decryptData(iv, text)
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(ruta)
                    binding.iButton2.setImageBitmap(imgBitmap)
                }
                if (i.calzado != null) {
                    val iv: ByteArray = Base64.decode(i.calzado!!.iv, Base64.DEFAULT)
                    val text: ByteArray = Base64.decode(i.calzado!!.encryptedRuta, Base64.DEFAULT)
                    val ruta = homeViewModel.decryptData(iv, text)
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(ruta)
                    binding.iButton3.setImageBitmap(imgBitmap)
                }
                if (i.cazadoras != null) {
                    val iv: ByteArray = Base64.decode(i.cazadoras!!.iv, Base64.DEFAULT)
                    val text: ByteArray = Base64.decode(i.cazadoras!!.encryptedRuta, Base64.DEFAULT)
                    val ruta = homeViewModel.decryptData(iv, text)
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(ruta)
                    binding.iButton4.setImageBitmap(imgBitmap)
                }
                if (i.jerseis != null) {
                    val iv: ByteArray = Base64.decode(i.jerseis!!.iv, Base64.DEFAULT)
                    val text: ByteArray = Base64.decode(i.jerseis!!.encryptedRuta, Base64.DEFAULT)
                    val ruta = homeViewModel.decryptData(iv, text)
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(ruta)
                    binding.iButton5.setImageBitmap(imgBitmap)
                }
                if (i.conjuntos != null) {
                    val iv: ByteArray = Base64.decode(i.conjuntos!!.iv, Base64.DEFAULT)
                    val text: ByteArray = Base64.decode(i.conjuntos!!.encryptedRuta, Base64.DEFAULT)
                    val ruta = homeViewModel.decryptData(iv, text)
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(ruta)
                    binding.iButton6.setImageBitmap(imgBitmap)
                }
            }
        })



        binding.accionCasual.setOnClickListener{
            seleccionContexto(temperatura, "CASUAL", llueve)
        }
        binding.accionDeporte.setOnClickListener{
            seleccionContexto(temperatura, "DEPORTE", llueve)
        }
        binding.accionFormal.setOnClickListener{
            seleccionContexto(temperatura, "FORMAL", llueve)
        }
        binding.accionPlaya.setOnClickListener{
            seleccionContexto(temperatura, "PLAYA", llueve)
        }

        binding.srl.setOnRefreshListener {
            coordenadas()
            binding.srl.isRefreshing = false
        }


        return view
    }

    @SuppressLint("SetTextI18n")
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
                    binding.textCity.text = w.name + ", " + w.sys.country
                    binding.tTemp.text = "$tempInt °C"
                    val sTermica = w.main.feels_like.toInt()
                    binding.sTermica.text = "Sensación Termica: $sTermica °C "
                    binding.textClima.text = w.weather[0].description
                    if (("09" in imgclima) || ("10" in imgclima) || ("11" in imgclima) || ("13" in imgclima)) {
                        llueve = true
                    }
                }
            }
        })
    }

    private fun coordenadas(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        if (context?.checkSelfPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && context?.checkSelfPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permisosCamara = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissions(permisosCamara, 1)

        }else{
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            val latitud = location.latitude.toString()
                            val longitud = location.longitude.toString()
                            llamarApi(latitud, longitud, binding)
                        }else{
                            Toast.makeText(requireActivity(), "No se ha obtenido Temperatura, Regarga la página", Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    private fun seleccionContexto(temp: Float?, ocasion: String, llueve: Boolean){
        if(temp!=null && ocasion!=""){
            setFragmentResult("ocasion", bundleOf("ocasion" to ocasion))
            setFragmentResult("temperatura", bundleOf("temperatura" to temp))
            setFragmentResult("lluvia", bundleOf("lluvia" to llueve))
            findNavController().navigate(R.id.action_navigation_home_to_elegirOutfitFragment)
        }
        else{
            Toast.makeText(requireActivity(), "No se ha obtenido Temperatura", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}