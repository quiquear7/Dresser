package com.uc3m.dresser.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uc3m.dresser.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val spnCategorias = root.findViewById<Spinner>(R.id.categorias)
        val lCategorias = resources.getStringArray(R.array.s_categorias)

        val aCat = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lCategorias)
        spnCategorias.adapter = aCat

        val spnColores = root.findViewById<Spinner>(R.id.colores)
        val lColores = resources.getStringArray(R.array.s_colores)

        val aCol = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lColores)
        spnColores.adapter = aCol

        spnCategorias.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Toast.makeText(requireActivity(),lCategorias[position],Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        spnColores.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Toast.makeText(requireActivity(),lColores[position],Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        val botonCamara = root.findViewById<Button>(R.id.bCamara)
        botonCamara.setOnClickListener() {
            /*val REQUEST_IMAGE_CAPTURE = 1
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }*/
            Toast.makeText(requireActivity(),"hola",Toast.LENGTH_SHORT).show()
        }



        return root
    }

/*

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data.extras.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }*/



}