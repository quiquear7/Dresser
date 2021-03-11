package com.uc3m.dresser.ui.listadapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.uc3m.dresser.R
import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.databinding.RecyclerViewItemBinding

class ListaAdapter: RecyclerView.Adapter<ListaAdapter.MyViewHolder>() {
    private var prendaList = emptyList<Prenda>()

    class MyViewHolder(val binding: RecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent,
            false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        val item = prendaList[position]
        with(holder){
            if(item.ruta!="*"){
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(item.ruta)
                binding.iButton.setImageBitmap(imgBitmap)
            }
            binding.tCategoria.text = item.categoria
            binding.tColor.text = item.color
            binding.iButton.setOnClickListener{
                Log.i("imagen", "boton pulsado")
            }
            binding.tNombre.text = item.nombre
            binding.tOcasion.text = item.ocasion
        }
    }


    override fun getItemCount(): Int {
        return prendaList.size
    }

    fun setData(prendaList: List<Prenda>){
        this.prendaList = prendaList
        notifyDataSetChanged()
    }
}