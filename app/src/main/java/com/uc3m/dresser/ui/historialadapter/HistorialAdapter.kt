package com.uc3m.dresser.ui.historialadapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.database.Registro
import com.uc3m.dresser.databinding.HistorialItemBinding

class HistorialAdapter: RecyclerView.Adapter<HistorialAdapter.MyViewHolder>()  {
    private var registroList = emptyList<Prenda>()

    class MyViewHolder(val binding: HistorialItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HistorialItemBinding.inflate(LayoutInflater.from(parent.context), parent,
                false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        val item = registroList[position]
        with(holder){
            val imgBitmap: Bitmap =  BitmapFactory.decodeFile(item.ruta)
            binding.imageView.setImageBitmap(imgBitmap)
        }
    }


    override fun getItemCount(): Int {
        return registroList.size
    }

    fun setData(registroList: List<Registro>){
       /* for ( i in registroList) {
            for (x in i.prenda){
                this.registroList += x
            }
        }*/

        notifyDataSetChanged()
    }
}