package com.uc3m.dresser.ui.dashboard

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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

    private var foto: Uri? = null
    private var categoria =  ""
    private var color =  ""
    private val REQUEST_IMAGE_CAPTURE = 1

    private var imgFoto: ImageView? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)


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
                categoria = lCategorias[position]
                //Toast.makeText(requireActivity(),lCategorias[position],Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        spnColores.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                color = lColores[position]
                //Toast.makeText(requireActivity(),lColores[position],Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        imgFoto = root.findViewById<ImageView>(R.id.imgFoto)

        val botonCamara = root.findViewById<Button>(R.id.bCamara)
        botonCamara.setOnClickListener() {
            permisos()
        }

        val botonAdd = root.findViewById<Button>(R.id.bagregar)
        botonAdd.setOnClickListener(){
            imgFoto?.setImageURI(null)
            Toast.makeText(requireActivity(),"ADD",Toast.LENGTH_SHORT).show()
        }
        return root
    }

    private fun permisos(){
        if(context?.checkSelfPermission(android.Manifest.permission.CAMERA)  == PackageManager.PERMISSION_DENIED
                || context?.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            val permisosCamara = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permisosCamara,REQUEST_IMAGE_CAPTURE)
        }
        else{
            abrirCamara()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            REQUEST_IMAGE_CAPTURE ->{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    abrirCamara()
                }
                else{
                    Toast.makeText(requireActivity(),"No puedes acceder a la cámara",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode,resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
            imgFoto?.setImageURI(foto)
        }
    }

    private fun  abrirCamara(){
        val value = ContentValues()
        value.put(MediaStore.Images.Media.TITLE, "Nueva imagen")
        foto = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,value)
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, foto)
        startActivityForResult(camaraIntent,REQUEST_IMAGE_CAPTURE )
    }

}