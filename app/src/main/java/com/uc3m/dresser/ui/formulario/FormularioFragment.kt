package com.uc3m.dresser.ui.formulario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentFormularioBinding
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.navigateUp
import com.uc3m.dresser.R.*

class FormularioFragment : Fragment() {

    private lateinit var binding: FragmentFormularioBinding

    public var ocasion = ""

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
            findNavController().navigate(R.id.action_formularioFragment_to_navigation_home)
        }
        return view
    }



   // override fun onSupportNavigateUp() = findNavController(R.id.listaFragment).navigateUp()

}