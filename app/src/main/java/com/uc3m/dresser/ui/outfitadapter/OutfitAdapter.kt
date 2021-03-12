package com.uc3m.dresser.ui.outfitadapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uc3m.dresser.database.Combinacion
import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.database.Registro
import com.uc3m.dresser.databinding.OutfitItemBinding
import com.uc3m.dresser.ui.SendData
import com.uc3m.dresser.viewModels.PrendaViewModel
import java.text.SimpleDateFormat
import java.util.*

class OutfitAdapter(listener: SendData): RecyclerView.Adapter<OutfitAdapter.MyViewHolder>() {
    private var outfitList = emptyList<Combinacion>()
    private lateinit var prendaViewModel: PrendaViewModel
    private lateinit var registro: Registro
    private  var listener: SendData = listener

    class MyViewHolder(val binding: OutfitItemBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
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
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.parteSuperior!!.ruta)
                binding.iButton1.setImageBitmap(imgBitmap)
                binding.tNombre1.text = i.parteSuperior!!.nombre
                prenda += i.parteSuperior
            }
            if(i.parteInferior!=null){
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.parteInferior!!.ruta)
                binding.iButton2.setImageBitmap(imgBitmap)
                binding.tNombre2.text = i.parteInferior!!.nombre
                prenda += i.parteInferior
            }
            if(i.calzado!=null){
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.calzado!!.ruta)
                binding.iButton3.setImageBitmap(imgBitmap)
                binding.tNombre3.text = i.calzado!!.nombre
                prenda += i.calzado
            }
            if(i.cazadoras!=null){
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.cazadoras!!.ruta)
                binding.iButton4.setImageBitmap(imgBitmap)
                binding.tNombre4.text = i.cazadoras!!.nombre
                prenda += i.cazadoras
            }
            if(i.jerseis!=null){
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.jerseis!!.ruta)
                binding.iButton5.setImageBitmap(imgBitmap)
                binding.tNombre5.text = i.jerseis!!.nombre
                prenda += i.jerseis
            }
            if(i.conjuntos!=null){
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(i.conjuntos!!.ruta)
                binding.iButton6.setImageBitmap(imgBitmap)
                binding.tNombre6.text = i.conjuntos!!.nombre
                prenda += i.conjuntos
            }

            binding.confirmarOutfit.setOnClickListener {
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())
                registro = Registro(0,currentDate, prenda as List<Prenda>)
                listener.sendInfo(registro)
            }
        }
    }


    override fun getItemCount(): Int {
        return outfitList.size
    }

    fun setData(outfit: List<Prenda>, temperatura: Float){
        Log.i("list",outfit.toString())
        this.outfitList =  generarOutfits(outfit, temperatura)
        Log.i("list",this.outfitList.toString())
        notifyDataSetChanged()
    }

    private fun generarOutfits(outfitList: List<Prenda>, temperatura: Float): List<Combinacion> {

        val combinaciones = emptyList<Combinacion>().toMutableList()
        var prendasNecesarias = 4
        Log.i("temperatura", temperatura.toString())
        /*
        if(temperatura>15 && temperatura<=25){
            prendasNecesarias = 4
        }
        if(temperatura>25){
            prendasNecesarias = 3
        }*/

        var cont = 0
        var registro  = Combinacion(null, null, null, null, null, null)
        for (i in outfitList){
            Log.i("cont", cont.toString())
            Log.i("Combinacion", registro.toString())
            if((i.categoria=="CAMISA M. LARGA" || i.categoria=="CAMISA M. CORTA"
                    || i.categoria=="CAMISETAS M. CORTA" || i.categoria=="CAMISETAS M. LARGA"
                    || i.categoria=="CAMISETAS TIRANTES" || i.categoria=="POLOS"
                    || i.categoria=="TOPS") && registro.parteSuperior == null ) {
                        registro.parteSuperior=i
                        cont++
            }
            if ((i.categoria == "PANTALONES" || i.categoria == "PANTALONES CORTOS"
                    || i.categoria=="FALDAS" || i.categoria == "JEANS") && registro.parteInferior==null ){
                        registro.parteInferior = i
                        cont++
            }
            if((i.categoria == "DEPORTIVAS" || i.categoria == "ZAPATOS"
                    || i.categoria == "BOTAS Y BOTINES" || i.categoria == "SANDALIAS") && registro.calzado==null){
                        registro.calzado = i
                        cont++
            }
            if ((i.categoria=="ABRIGOS" || i.categoria=="CAZADORAS" || i.categoria=="CHUBASQUERO"
                    || i.categoria=="BLAZERS") && registro.cazadoras == null) {
                        registro.cazadoras=i
                        cont++
            }
            if ((i.categoria=="SOBRECAMISAS" || i.categoria=="CHALECOS" || i.categoria=="CHALECOS"
                    || i.categoria=="JERSÉIS" || i.categoria=="CHAQUETAS" || i.categoria=="SUDADERAS")
                    && registro.jerseis == null){
                registro.jerseis = i
                cont++
            }
            if((i.categoria=="VESTIDOS" || i.categoria=="MONOS") && registro.conjuntos == null){
                registro.conjuntos = i
                if(prendasNecesarias==3 && cont+2<=prendasNecesarias){
                    cont += 2
                }
                if(prendasNecesarias>=4&& cont+3<=prendasNecesarias){
                    cont += 3
                }
            }
            if(i.categoria=="TRAJES" && registro.conjuntos == null){
                registro.conjuntos = i
                if(prendasNecesarias==3 && cont+2<=prendasNecesarias){
                    cont += 2
                }
                if(prendasNecesarias>=4 && cont+3<=prendasNecesarias){
                    cont += 3
                }
            }

            if (cont == prendasNecesarias){
                combinaciones += registro
                registro  = Combinacion(null, null, null, null, null, null)
                cont = 0
            }
        }
        return combinaciones
    }


}