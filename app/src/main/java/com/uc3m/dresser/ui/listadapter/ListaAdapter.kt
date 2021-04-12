package com.uc3m.dresser.ui.listadapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.databinding.RecyclerViewItemBinding
import com.uc3m.dresser.ui.SendData
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


class ListaAdapter(listener: SendPrenda): RecyclerView.Adapter<ListaAdapter.MyViewHolder>() {
    private var prendaList = emptyList<Prenda>()
    private val viewBinderHelper = ViewBinderHelper()
    private  var listener: SendPrenda = listener

    class MyViewHolder(val binding: RecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecyclerViewItemBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        val item = prendaList[position]

        with(holder){
            viewBinderHelper.setOpenOnlyOne(true)
            viewBinderHelper.bind(holder.binding.swipeLayout, prendaList[position].id.toString())
            viewBinderHelper.closeLayout(prendaList[position].id.toString())
            val iv: ByteArray = Base64.decode(item.iv, Base64.DEFAULT)
            val text: ByteArray = Base64.decode(item.encryptedRuta, Base64.DEFAULT)
            val ruta = decryptData(iv, text)
            val imgBitmap: Bitmap =  BitmapFactory.decodeFile(ruta)
            binding.iButton.setImageBitmap(imgBitmap)
            binding.tCategoria.text = item.categoria
            binding.tColor.text = item.color
            binding.tNombre.text = item.nombre
            binding.tOcasion.text = item.ocasion
            binding.editButton.setOnClickListener{
                listener.sendInfo(item, 0)
            }
            binding.deleteButton.setOnClickListener{
                listener.sendInfo(item, 1)
            }
        }

    }


    override fun getItemCount(): Int {
        return prendaList.size
    }

    fun setData(prendaList: List<Prenda>){
        this.prendaList = prendaList
        notifyDataSetChanged()
    }

    fun saveStates(outState: Bundle?) {
        viewBinderHelper.saveStates(outState)
    }

    fun restoreStates(inState: Bundle?) {
        viewBinderHelper.restoreStates(inState)
    }

    fun getKey(): SecretKey {
        val keystore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)
        val secretKeyEntry = keystore.getEntry("MyKeyStore", null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }


    fun decryptData(ivBytes: ByteArray, data: ByteArray) : String{
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val spec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, getKey(),spec)
        return cipher.doFinal(data).toString(Charsets.UTF_8).trim()
    }
}