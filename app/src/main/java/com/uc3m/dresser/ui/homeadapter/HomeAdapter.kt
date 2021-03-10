package com.uc3m.dresser.ui.homeadapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.database.Registro
import com.uc3m.dresser.databinding.HomeItemBinding
import com.uc3m.dresser.databinding.OutfitItemBinding
import com.uc3m.dresser.ui.SendData
import com.uc3m.dresser.ui.elegiroutfit.ElegirOutfitFragment
import com.uc3m.dresser.viewModels.PrendaViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeAdapter(): RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    private var outfitList = emptyList<Prenda>()
    private lateinit var prendaViewModel: PrendaViewModel
    private lateinit var registro: Registro
    var cont = 0
    class MyViewHolder(val binding: HomeItemBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HomeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        val item = outfitList[position]
        var prenda = emptyList<Prenda>()

        with(holder){
            Log.i("ruta", item.ruta.toString())
            if(cont==0){
                for ( i in outfitList){
                    val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.ruta)
                    if (i.categoria=="SOBRECAMISAS") {
                        binding.iButton1.setImageBitmap(imgBitmap)
                        binding.tNombre1.text = i.nombre
                        prenda += i
                    }
                    if (i.categoria=="PANTALONES"){
                        binding.iButton2.setImageBitmap(imgBitmap)
                        binding.tNombre2.text = i.nombre
                        prenda += i
                    }
                    if(i.categoria == "DEPORTIVAS"){
                        binding.iButton3.setImageBitmap(imgBitmap)
                        binding.tNombre3.text = i.nombre
                        prenda += i
                    }
                    cont ++
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return outfitList.size
    }

    fun setData(outfitList: List<Prenda>){
        this.outfitList = outfitList
        notifyDataSetChanged()
    }






}