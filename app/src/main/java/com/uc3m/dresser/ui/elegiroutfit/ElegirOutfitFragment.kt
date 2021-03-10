package com.uc3m.dresser.ui.elegiroutfit

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentElegirOutfitBinding
import com.uc3m.dresser.ui.formulario.FormularioFragment
import com.uc3m.dresser.ui.historialadapter.HistorialAdapter
import com.uc3m.dresser.ui.listadapter.ListaAdapter
import com.uc3m.dresser.ui.outfitadapter.OutfitAdapter
import com.uc3m.dresser.viewModels.PrendaViewModel
import java.lang.reflect.Array.newInstance
import javax.xml.datatype.DatatypeFactory.newInstance
import javax.xml.parsers.DocumentBuilderFactory.newInstance
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.uc3m.dresser.database.Registro
import com.uc3m.dresser.ui.SendData


class ElegirOutfitFragment :  Fragment(), SendData {
    private lateinit var binding: FragmentElegirOutfitBinding
    private lateinit var prendaViewModel: PrendaViewModel
    var ocasion: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentElegirOutfitBinding.inflate(inflater, container, false)


        prendaViewModel = ViewModelProvider(this).get(PrendaViewModel::class.java)



        /*if(adapter.boton){
            findNavController().navigate(R.id.action_elegirOutfitFragment_to_navigation_home)
        }*/
        
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use the Kotlin extension in the fragment-ktx artifact
        setFragmentResultListener("requestKey") { key, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            ocasion = bundle.getString("bundleKey").toString()
            Log.i("hola", ocasion)


            val adapter = OutfitAdapter(this)

            val recyclerView = binding.recyclerView

            recyclerView.setHasFixedSize(true)

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            prendaViewModel.readOcasion(ocasion).observe(viewLifecycleOwner, {prendas->
                adapter.setData(prendas)
            })

        }
    }



    override fun sendInfo(registro: Registro) {
        Log.i("lo has conseguido", registro.toString())
        prendaViewModel.addRegistro(registro)
        findNavController().navigate(R.id.action_elegirOutfitFragment_to_navigation_home)
    }


}