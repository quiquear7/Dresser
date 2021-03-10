package com.uc3m.dresser.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uc3m.dresser.R
import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.databinding.FragmentDashboardBinding
import com.uc3m.dresser.viewModels.PrendaViewModel
import java.io.File


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var binding: FragmentDashboardBinding

    private var foto: Uri? = null
    private var categoria =  ""
    private var color =  ""
    private var ruta = ""
    private var ocasion = ""
    private var estampado =  ""
    private val REQUEST_IMAGE_CAPTURE = 1

    private var imgFoto: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)


        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        val spnCategorias = binding.categorias
        val lCategorias = resources.getStringArray(R.array.s_categorias)

        val aCat = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lCategorias)
        spnCategorias.adapter = aCat

        spnCategorias.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                categoria = lCategorias[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val spnColores = binding.colores
        val lColores = resources.getStringArray(R.array.s_colores)

        val aCol = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lColores)
        spnColores.adapter = aCol



        spnColores.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                color = lColores[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val spnEstampado = binding.estampados
        val lEstampados = resources.getStringArray(R.array.s_estampados)

        val aEst = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lEstampados)
        spnEstampado.adapter = aEst

        spnEstampado.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                estampado = lEstampados[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val spnOcasion = binding.ocasion
        val lOcasion = resources.getStringArray(R.array.s_ocasion)

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

        imgFoto = binding.imgFoto

        val botonCamara = binding.bCamara
        botonCamara.setOnClickListener() {
            if (context?.checkSelfPermission(android.Manifest.permission.CAMERA)  == PackageManager.PERMISSION_DENIED
                || context?.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permisosCamara = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permisosCamara,REQUEST_IMAGE_CAPTURE)
            }else{
                abrirCamara()
            }
        }

        val botonAdd = binding.bagregar
        botonAdd.setOnClickListener(){
            val nombre = binding.nameText.text.toString()
            if(foto!=null || nombre != ""){
                val prendaViewModel = ViewModelProvider(this).get(PrendaViewModel::class.java)
                val prenda = Prenda(0,nombre, categoria, color, estampado, ocasion, ruta)
                prendaViewModel.addStudent(prenda)
                imgFoto?.setImageURI(null)
                foto = null
                binding.nameText.text.clear()
                Toast.makeText(requireActivity(),"Add Completed",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireActivity(),"Image Required",Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

   /* private fun permisos(){
        if(context?.checkSelfPermission(android.Manifest.permission.CAMERA)  == PackageManager.PERMISSION_DENIED
                || context?.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            val permisosCamara = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permisosCamara,REQUEST_IMAGE_CAPTURE)
        }
        else{
            abrirCamara()
        }
    }*/

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
       /* super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            REQUEST_IMAGE_CAPTURE ->{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    abrirCamara()
                }
                else{
                    Toast.makeText(requireActivity(),"No puedes acceder a la cámara",Toast.LENGTH_SHORT).show()
                    val permisosCamara = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permisosCamara,REQUEST_IMAGE_CAPTURE)
                }
            }
        }*/
        abrirCamara()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode,resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
            imgFoto?.setImageURI(foto)
        }
    }

    private fun  abrirCamara(){
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var imagen: File? = crearImagen()
        if(imagen != null){
            foto = context?.let { FileProvider.getUriForFile(it, "com.uc3m.dresser.ui.dashboard.fileprovider", imagen) }
            camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, foto)
            startActivityForResult(camaraIntent,REQUEST_IMAGE_CAPTURE )
        }
    }

    private fun crearImagen() : File?{
        val nombre =  "foto_"
        val directorio = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imagen = File.createTempFile(nombre, ".jpg", directorio)
        ruta =  imagen.absolutePath
        return imagen
    }


}