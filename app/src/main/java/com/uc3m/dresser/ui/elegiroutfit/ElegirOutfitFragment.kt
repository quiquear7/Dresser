package com.uc3m.dresser.ui.elegiroutfit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentElegirOutfitBinding
import com.uc3m.dresser.ui.outfitadapter.OutfitAdapter
import com.uc3m.dresser.viewModels.PrendaViewModel
import androidx.fragment.app.setFragmentResultListener
import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.database.Registro
import com.uc3m.dresser.ui.SendData
import java.text.SimpleDateFormat
import java.util.*


class ElegirOutfitFragment :  Fragment(), SendData {
    private lateinit var elegirOutfitViewModel: ElegirOutfitViewModel
    private lateinit var binding: FragmentElegirOutfitBinding
    private lateinit var prendaViewModel: PrendaViewModel
    var ocasion: String = ""
    var llueve: Boolean = false
    var temperatura: Float? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentElegirOutfitBinding.inflate(inflater, container, false)
        prendaViewModel = ViewModelProvider(this).get(PrendaViewModel::class.java)
        elegirOutfitViewModel = ViewModelProvider(this).get(ElegirOutfitViewModel::class.java)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use the Kotlin extension in the fragment-ktx artifact
        setFragmentResultListener("ocasion") { key, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            ocasion = bundle.getString("ocasion").toString()
            setFragmentResultListener("lluvia") { key, bundle ->
                llueve = bundle.getBoolean("lluvia")
                setFragmentResultListener("temperatura") { key, bundle ->
                    // We use a String here, but any type that can be put in a Bundle is supported
                    temperatura = bundle.getFloat("temperatura")

                    val adapter = OutfitAdapter(this)
                    val recyclerView = binding.recyclerView
                    recyclerView.setHasFixedSize(true)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())

                    val sdf = SimpleDateFormat("yyyy/MM/dd")
                    val currentDate = sdf.format(Date())
                    val fechas = currentDate.split("/")
                    val fecha = Date(fechas[0].toInt() , fechas[1].toInt(), fechas[2].toInt())

                    prendaViewModel.readOcasion(ocasion, fecha.time).observe(viewLifecycleOwner, {prendas->
                        if(temperatura!=null){
                            val list = elegirOutfitViewModel.generarOutfits(prendas, temperatura!!, llueve)
                            adapter.setData(list)
                        } else{
                            Toast.makeText(requireActivity(),"No se ha obtenido Temperatura", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    override fun sendInfo(registro: Registro, date: String) {
        prendaViewModel.addRegistro(registro)
        updatePrenda(registro.prenda.cazadoras, date)
        updatePrenda(registro.prenda.calzado, date)
        updatePrenda(registro.prenda.conjuntos, date)
        updatePrenda(registro.prenda.jerseis, date)
        updatePrenda(registro.prenda.parteInferior, date)
        updatePrenda(registro.prenda.parteSuperior, date)

        findNavController().navigate(R.id.action_elegirOutfitFragment_to_navigation_home)
    }

    private fun updatePrenda(prenda: Prenda?, date:String){
        if(prenda!=null){
            val currentPrenda: Prenda = prenda
            if (date != null) {
                val fechas = date.split("/")
                val fecha = Date(fechas[0].toInt() , fechas[1].toInt(), fechas[2].toInt())
                currentPrenda.ultimoUso = fecha.time
                prendaViewModel.updatePrenda(currentPrenda)
            }
        }
    }


}