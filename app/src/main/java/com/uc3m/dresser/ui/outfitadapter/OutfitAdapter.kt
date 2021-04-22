package com.uc3m.dresser.ui.outfitadapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.uc3m.dresser.database.Combinacion
import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.database.Registro
import com.uc3m.dresser.databinding.OutfitItemBinding
import com.uc3m.dresser.ui.SendData
import com.uc3m.dresser.viewModels.PrendaViewModel
import java.security.KeyStore
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class OutfitAdapter(listener: SendData): RecyclerView.Adapter<OutfitAdapter.MyViewHolder>() {
    private lateinit var auth: FirebaseAuth
    private var outfitList = emptyList<Combinacion>()
    private lateinit var prendaViewModel: PrendaViewModel
    private lateinit var registro: Registro
    private  var listener: SendData = listener

    class MyViewHolder(val binding: OutfitItemBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        auth = FirebaseAuth.getInstance()
        val binding = OutfitItemBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        val i = outfitList[position]
        val prenda = emptyList<Prenda?>().toMutableList()
        with(holder){
            Log.i("prenda", i.toString())
            if(i.parteSuperior!=null){
                val iv: ByteArray = Base64.decode(i.parteSuperior!!.iv, Base64.DEFAULT)
                val text: ByteArray = Base64.decode(i.parteSuperior!!.encryptedRuta, Base64.DEFAULT)
                val ruta = decryptData(iv, text)
                if(ruta !="") {
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(ruta)
                    binding.iButton1.setImageBitmap(imgBitmap)
                    binding.tNombre1.text = i.parteSuperior!!.nombre
                    prenda += i.parteSuperior
                }
            }
            if(i.parteInferior!=null){
                val iv: ByteArray = Base64.decode(i.parteInferior!!.iv, Base64.DEFAULT)
                val text: ByteArray = Base64.decode(i.parteInferior!!.encryptedRuta, Base64.DEFAULT)
                val ruta = decryptData(iv, text)
                if(ruta !="") {
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(ruta)
                    binding.iButton2.setImageBitmap(imgBitmap)
                    binding.tNombre2.text = i.parteInferior!!.nombre
                    prenda += i.parteInferior
                }
            }
            if(i.calzado!=null){
                val iv: ByteArray = Base64.decode(i.calzado!!.iv, Base64.DEFAULT)
                val text: ByteArray = Base64.decode(i.calzado!!.encryptedRuta, Base64.DEFAULT)
                val ruta = decryptData(iv, text)
                if(ruta !="") {
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(ruta)
                    binding.iButton3.setImageBitmap(imgBitmap)
                    binding.tNombre3.text = i.calzado!!.nombre
                    prenda += i.calzado
                }
            }
            if(i.cazadoras!=null){
                val iv: ByteArray = Base64.decode(i.cazadoras!!.iv, Base64.DEFAULT)
                val text: ByteArray = Base64.decode(i.cazadoras!!.encryptedRuta, Base64.DEFAULT)
                val ruta = decryptData(iv, text)
                if(ruta !="") {
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(ruta)
                    binding.iButton4.setImageBitmap(imgBitmap)
                    binding.tNombre4.text = i.cazadoras!!.nombre
                    prenda += i.cazadoras
                }
            }
            if(i.jerseis!=null){
                val iv: ByteArray = Base64.decode(i.jerseis!!.iv, Base64.DEFAULT)
                val text: ByteArray = Base64.decode(i.jerseis!!.encryptedRuta, Base64.DEFAULT)
                val ruta = decryptData(iv, text)
                if(ruta !="") {
                    val imgBitmap: Bitmap = BitmapFactory.decodeFile(ruta)
                    binding.iButton5.setImageBitmap(imgBitmap)
                    binding.tNombre5.text = i.jerseis!!.nombre
                    prenda += i.jerseis
                }
            }

            binding.confirmarOutfit.setOnClickListener {
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())
                registro = Registro(0,currentDate, i)
                listener.sendInfo(registro, currentDate)
            }
        }
    }


    override fun getItemCount(): Int {
        return outfitList.size
    }

    fun setData(outfit: List<Combinacion>){
        this.outfitList =  outfit
        notifyDataSetChanged()
    }

    fun getKey(): SecretKey? {
        val keystore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)
        return (keystore.getEntry(auth.currentUser.email, null) as KeyStore.SecretKeyEntry?)?.secretKey
    }


    fun decryptData(ivBytes: ByteArray, data: ByteArray) : String{
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val spec = IvParameterSpec(ivBytes)
        return if(getKey()!=null){
            cipher.init(Cipher.DECRYPT_MODE, getKey(),spec)
            cipher.doFinal(data).toString(Charsets.UTF_8).trim()
        }else{
            ""
        }

    }
}


