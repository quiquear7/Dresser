package com.uc3m.dresser.ui.ropa

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.uc3m.dresser.R
import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.databinding.FragmentRopaBinding
import com.uc3m.dresser.viewModels.PrendaViewModel
import java.io.File
import java.util.*

class RopaFragment : Fragment() {

    private lateinit var ropaViewModel: RopaViewModel
    private lateinit var binding: FragmentRopaBinding
    private var idPrenda: Int? = null
    private lateinit var spnEstampado: Spinner
    private lateinit var spnCategorias: Spinner
    private lateinit var spnColores: Spinner
    private lateinit var spnOcasion: Spinner

    private var foto: Uri? = null
    private var categoria =  ""
    private var color =  ""
    private var ruta = ""
    private var ocasion = ""
    private var estampado =  ""
    private val REQUEST_IMAGE_CAPTURE = 1
    private val PHOTO_SELECTED = 2
    private var ultimoUso: Long = 0
    private var imgFoto: ImageView? = null
    private lateinit var prendaViewModel: PrendaViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ropaViewModel = ViewModelProvider(this).get(RopaViewModel::class.java)
        prendaViewModel = ViewModelProvider(this).get(PrendaViewModel::class.java)
        binding = FragmentRopaBinding.inflate(inflater, container, false)
        val view = binding.root

        spnCategorias = binding.categorias
        spnColores = binding.colores
        spnEstampado = binding.estampados
        spnOcasion = binding.ocasion

        setFragmentResultListener("envioPrenda") { key, bundle ->
            val id = bundle.getInt("prenda")
            prendaViewModel.readId(id).observe(viewLifecycleOwner, {prendas->
                idPrenda = prendas.id
                llenarInfo(prendas)
            })
        }


        val lCategorias = resources.getStringArray(R.array.s_categorias)

        val aCat = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lCategorias)
        spnCategorias.adapter = aCat

        spnCategorias.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                categoria = lCategorias[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        val lColores = resources.getStringArray(R.array.s_colores)

        val aCol = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lColores)
        spnColores.adapter = aCol
        spnColores.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                color = lColores[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        val lEstampados = resources.getStringArray(R.array.s_estampados)

        val aEst = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lEstampados)
        spnEstampado.adapter = aEst


        spnEstampado.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                estampado = lEstampados[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        val lOcasion = resources.getStringArray(R.array.s_ocasion)

        val aOc = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lOcasion)
        spnOcasion.adapter = aOc
        spnOcasion.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ocasion = lOcasion[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

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
            if (idPrenda!=null){
                val pair = ropaViewModel.encrypData(ruta)
                val encodedIV: String = Base64.encodeToString(pair.first, Base64.DEFAULT)
                val encodedText: String = Base64.encodeToString(pair.second, Base64.DEFAULT)
                val prenda = Prenda(idPrenda!!,nombre, categoria, color, estampado, ocasion, ultimoUso,encodedIV, encodedText)
                prendaViewModel.updatePrenda(prenda)

                imgFoto?.setImageURI(null)
                foto = null
                ruta = ""
                binding.nameText.text.clear()
                Toast.makeText(requireActivity(),"Updated Completed", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_ropaFragment_to_listaFragment)
            }

        }

       /* binding.fabGallery.setOnClickListener{
            if ( context?.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permisosLectura = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permisosLectura,PHOTO_SELECTED)
            }else{
                abrirGaleria()
            }
        }*/
        return view
    }

    private fun llenarInfo(prenda: Prenda) {
        val iv: ByteArray = Base64.decode(prenda.iv, Base64.DEFAULT)
        val text: ByteArray = Base64.decode(prenda.encryptedRuta, Base64.DEFAULT)
        val rutaTemp = ropaViewModel.decryptData(iv, text)
        val imgBitmap: Bitmap =  BitmapFactory.decodeFile(rutaTemp)
        binding.imgFoto.setImageBitmap(imgBitmap)
        binding.nameText.setText(prenda.nombre)
        categoria = prenda.categoria
        color =  prenda.color
        estampado = prenda.estampado
        ocasion = prenda.ocasion
        ultimoUso = prenda.ultimoUso
        ruta = rutaTemp

        val lCategorias = resources.getStringArray(R.array.s_categorias)
        spnCategorias.setSelection(lCategorias.indexOf(categoria),true)

        val lColores = resources.getStringArray(R.array.s_colores)
        spnColores.setSelection(lColores.indexOf(color),true)

        val lEstampado = resources.getStringArray(R.array.s_estampados)
        spnEstampado.setSelection(lEstampado.indexOf(estampado),true)

        val lOcasion = resources.getStringArray(R.array.s_ocasion)
        spnOcasion.setSelection(lOcasion.indexOf(ocasion),true)
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /* when (requestCode){
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
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            abrirCamara()
        }
        if(requestCode == PHOTO_SELECTED ){
            abrirGaleria()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode,resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
            imgFoto?.setImageURI(foto)
        }
        if(resultCode == Activity.RESULT_OK && requestCode == PHOTO_SELECTED ){
            if (data != null) {
                foto = data.data
                if (foto != null) {
                    val list = foto!!.path?.split(":")
                    // ruta = "/storage/emulated/0/"+list?.get(1).toString()
                    ruta ="*"
                    Log.i("ruta nueva", ruta)
                    imgFoto?.setImageURI(foto)
                }
            }
        }
    }

    private fun  abrirCamara(){
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imagen: File? = crearImagen()
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

    private fun  abrirGaleria(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PHOTO_SELECTED)
    }

}