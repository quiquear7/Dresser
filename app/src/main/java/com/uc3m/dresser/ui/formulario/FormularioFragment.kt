package com.uc3m.dresser.ui.formulario

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentFormularioBinding
import com.uc3m.dresser.R.*
import com.uc3m.dresser.ui.elegiroutfit.ElegirOutfitFragment
import com.uc3m.dresser.ui.outfitadapter.OutfitAdapter
import com.uc3m.dresser.viewModels.PrendaViewModel
import java.text.SimpleDateFormat
import java.util.*

class FormularioFragment : Fragment() {



    private lateinit var binding: FragmentFormularioBinding

    private var ocasion = "CASUAL"
    private var temperatura: Float? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentFormularioBinding.inflate(inflater, container, false)
        val view = binding.root

        val spnOcasion = binding.gocasion
        val lOcasion = resources.getStringArray(array.s_ocasion)

        val aOc = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lOcasion)
        spnOcasion.adapter = aOc

        spnOcasion.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ocasion = lOcasion[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.generar.setOnClickListener(){
            if(temperatura!=null && ocasion!=""){
                setFragmentResult("ocasion", bundleOf("ocasion" to ocasion))
                setFragmentResult("temperatura", bundleOf("temperatura" to temperatura))
                findNavController().navigate(R.id.action_formularioFragment_to_elegirOutfitFragment)
            }
            else{
                Toast.makeText(requireActivity(),"No se ha obtenido Temperatura",Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use the Kotlin extension in the fragment-ktx artifact
        setFragmentResultListener("envioTemp") { key, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            temperatura = bundle.getFloat("temperatura")
        }
    }







}


