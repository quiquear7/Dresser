package com.uc3m.dresser.ui.historialadapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uc3m.dresser.database.Combinacion
import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.database.Registro
import com.uc3m.dresser.databinding.HistorialItemBinding

class HistorialAdapter: RecyclerView.Adapter<HistorialAdapter.MyViewHolder>()  {
    private var outfitList = emptyList<Combinacion>()

    class MyViewHolder(val binding: HistorialItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HistorialItemBinding.inflate(LayoutInflater.from(parent.context), parent,
                false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        val i = outfitList[position]
        with(holder){
            if(i.parteSuperior!=null){
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.parteSuperior!!.ruta)
                binding.iButton1.setImageBitmap(imgBitmap)
                binding.tNombre1.text = i.parteSuperior!!.nombre
            }
            if(i.parteInferior!=null){
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.parteInferior!!.ruta)
                binding.iButton2.setImageBitmap(imgBitmap)
                binding.tNombre2.text = i.parteInferior!!.nombre
            }
            if(i.calzado!=null){
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.calzado!!.ruta)
                binding.iButton3.setImageBitmap(imgBitmap)
                binding.tNombre3.text = i.calzado!!.nombre
            }
            if(i.cazadoras!=null){
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.cazadoras!!.ruta)
                binding.iButton4.setImageBitmap(imgBitmap)
                binding.tNombre4.text = i.cazadoras!!.nombre
            }
            if(i.jerseis!=null){
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.jerseis!!.ruta)
                binding.iButton5.setImageBitmap(imgBitmap)
                binding.tNombre5.text = i.jerseis!!.nombre
            }
            if(i.conjuntos!=null){
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.conjuntos!!.ruta)
                binding.iButton6.setImageBitmap(imgBitmap)
                binding.tNombre6.text = i.conjuntos!!.nombre
            }
        }
    }


    override fun getItemCount(): Int {
        return outfitList.size
    }

    fun setData(registroList: List<Registro>){
       for ( i in registroList) {
                this.outfitList += i.prenda
       }
        notifyDataSetChanged()
    }

    fun deleteData(){
        this.outfitList = emptyList<Combinacion>()
    }
}