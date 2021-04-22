package com.uc3m.dresser.ui.dashboard


import android.app.Activity
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.uc3m.dresser.R
import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.databinding.FragmentDashboardBinding
import com.uc3m.dresser.viewModels.PrendaViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.KeyGenerator


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var auth: FirebaseAuth
    private var foto: Uri? = null
    private var categoria = ""
    private var color = ""
    private var ruta = ""
    private var ocasion = ""
    private var estampado = ""
    private val REQUEST_IMAGE_CAPTURE = 1
    private val PHOTO_SELECTED = 2

    private var imgFoto: ImageView? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)


        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = FirebaseAuth.getInstance()
        if (!dashboardViewModel.checkKey()) {
            val keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore"
            )
            val keyGenParameterSpec = KeyGenParameterSpec
                    .Builder(
                            auth.currentUser.email,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }

        if (!context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA)!!) {
            binding.bCamara.hide()
        }

        val spnCategorias = binding.categorias
        val lCategorias = resources.getStringArray(R.array.s_categorias)

        val aCat = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                lCategorias
        )
        spnCategorias.adapter = aCat

        spnCategorias.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                categoria = lCategorias[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val spnColores = binding.colores
        val lColores = resources.getStringArray(R.array.s_colores)

        val aCol = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lColores)
        spnColores.adapter = aCol



        spnColores.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                color = lColores[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val spnEstampado = binding.estampados
        val lEstampados = resources.getStringArray(R.array.s_estampados)

        val aEst = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                lEstampados
        )
        spnEstampado.adapter = aEst

        spnEstampado.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                estampado = lEstampados[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val spnOcasion = binding.ocasion
        val lOcasion = resources.getStringArray(R.array.s_ocasion)

        val aOc = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lOcasion)
        spnOcasion.adapter = aOc

        spnOcasion.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                ocasion = lOcasion[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        imgFoto = binding.imgFoto

        val botonAdd = binding.bagregar
        botonAdd.setOnClickListener() {

            val nombre = binding.nameText.text.toString()
            if (foto != null && nombre != "" && ruta != "") {
                val pair = dashboardViewModel.encrypData(ruta)
                val encodedIV: String = Base64.encodeToString(pair.first, Base64.DEFAULT)
                val encodedText: String = Base64.encodeToString(pair.second, Base64.DEFAULT)
                val prendaViewModel = ViewModelProvider(this).get(PrendaViewModel::class.java)
                val fecha = Date(2021, 1, 31)
                val fechaLong = fecha.time
                val prenda = Prenda(
                        0,
                        nombre,
                        categoria,
                        color,
                        estampado,
                        ocasion,
                        fechaLong,
                        encodedIV,
                        encodedText
                )
                prendaViewModel.addPrenda(prenda)
                imgFoto?.setImageURI(null)
                foto = null
                ruta = ""
                binding.nameText.text.clear()
                Toast.makeText(requireActivity(), "Add Completed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Image and Name Required", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        binding.bCamara.setOnClickListener() {
            if (context?.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    || context?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            ) {
                val permisosCamara = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permisosCamara, REQUEST_IMAGE_CAPTURE)
            } else {
                abrirCamara()
            }
        }

        binding.fabGallery.setOnClickListener {
            if (context?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permisosLectura = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permisosLectura, PHOTO_SELECTED)
            } else {
                abrirGaleria()
            }
        }
        return view
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            abrirCamara()
        }
        if (requestCode == PHOTO_SELECTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            abrirGaleria()
        }
    }

    private fun abrirCamara() {
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imagen: File? = crearImagen()
        if (imagen != null) {
            foto = context?.let {
                FileProvider.getUriForFile(
                        it,
                        "com.uc3m.dresser.ui.dashboard.fileprovider",
                        imagen
                )
            }
            camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, foto)
            startActivityForResult(camaraIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun crearImagen(): File? {
        val nombre = "dresser_"
        val directorio = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imagen = File.createTempFile(nombre, ".jpg", directorio)
        ruta = imagen.absolutePath
        return imagen
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PHOTO_SELECTED)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (foto != null) {
                imgFoto?.setImageURI(foto)
            } else {
                Toast.makeText(requireActivity(), "Fallo al realizar imagen", Toast.LENGTH_SHORT).show()
            }
        }

        if (resultCode == RESULT_OK && requestCode == PHOTO_SELECTED) {
            if (data != null) {
                foto = data.data
                if (foto != null) {
                    val wholeID = DocumentsContract.getDocumentId(foto)
                    val id = wholeID.split(":").toTypedArray()[1]

                    val column = arrayOf(MediaStore.Images.Media.DATA)
                    val sel = MediaStore.Images.Media._ID + "=?"

                    val cursor: Cursor? = activity?.contentResolver?.query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, arrayOf(id), null
                    )

                    val columnIndex = cursor?.getColumnIndex(column[0])

                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            ruta = columnIndex?.let { cursor.getString(it) }.toString()
                        }
                    }
                    cursor?.close()

                    imgFoto?.setImageURI(foto)
                }
            }
        }
    }
}


