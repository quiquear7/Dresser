package com.uc3m.dresser.ui.elegiroutfit


import android.util.Log
import androidx.lifecycle.ViewModel
import com.uc3m.dresser.database.Combinacion
import com.uc3m.dresser.database.Prenda
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class ElegirOutfitViewModel : ViewModel() {

    fun checkKey(): Boolean{
        val keystore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)
        val secretKeyEntry = keystore.getEntry("MyKeyStore", null) as? KeyStore.SecretKeyEntry
        return if (secretKeyEntry != null) {
            secretKeyEntry.secretKey != null
        }
        else{
            false
        }
    }

    fun getKey(): SecretKey {
        val keystore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)
        val secretKeyEntry = keystore.getEntry("MyKeyStore", null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }

    fun encrypData(data: String): Pair<ByteArray, ByteArray>{
        val cipher: Cipher = Cipher.getInstance("AES/CBC/NoPadding")
        var temp: String = data
        while(temp.toByteArray().size % 16 != 0 )
            temp += "\u0020"
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val ivBytes = cipher.iv
        val encryptedBytes = cipher.doFinal(temp.toByteArray())
        return Pair(ivBytes, encryptedBytes)

    }

    fun decryptData(ivBytes: ByteArray, data: ByteArray) : String{
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val spec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, getKey(),spec)
        return cipher.doFinal(data).toString(Charsets.UTF_8).trim()
    }


    fun generarOutfits(outfitList: List<Prenda>, temperatura: Float, llueve: Boolean): List<Combinacion> {

        val combinaciones = emptyList<Combinacion>().toMutableList()
        var prendasNecesarias = 5

        if(temperatura>15 && temperatura<=25){
            prendasNecesarias = 4
        }
        if(temperatura>25){
            prendasNecesarias = 3
        }

        var parteSuperior = 0
        var parteInferior = 0
        var calzado = 0
        var jerseis = 0
        var cazadoras = 0
        var conjuntos = 0

        for (i in outfitList){
            if((i.categoria=="CAMISA M. LARGA" || i.categoria=="CAMISA M. CORTA"
                        || i.categoria=="CAMISETAS M. CORTA" || i.categoria=="CAMISETAS M. LARGA"
                        || i.categoria=="CAMISETAS TIRANTES" || i.categoria=="POLOS"
                        || i.categoria=="TOPS")) {
                parteSuperior++
            }
            if ((i.categoria == "PANTALONES" || i.categoria == "PANTALONES CORTOS"
                        || i.categoria=="FALDAS" || i.categoria == "JEANS")){
                parteInferior++
            }
            if((i.categoria == "DEPORTIVAS" || i.categoria == "ZAPATOS"
                        || i.categoria == "BOTAS Y BOTINES" || i.categoria == "SANDALIAS")){
                calzado++
            }
            if ((i.categoria=="ABRIGOS" || i.categoria=="CAZADORAS" || i.categoria=="CHUBASQUERO"
                        || i.categoria=="BLAZERS")) {
                cazadoras++
            }
            if ((i.categoria=="SOBRECAMISAS" || i.categoria=="CHALECOS" || i.categoria=="CHALECOS"
                        || i.categoria=="JERSÉIS" || i.categoria=="CHAQUETAS" || i.categoria=="SUDADERAS")){
                jerseis++
            }
            if((i.categoria=="VESTIDOS" || i.categoria=="MONOS" || i.categoria=="TRAJES")){
                conjuntos++
            }
        }

        val m = parteSuperior + parteInferior + calzado + jerseis + cazadoras + conjuntos
        val n = prendasNecesarias
        val comb = funFactorial((n+m-1))/(funFactorial(m)*funFactorial(n-1))

        for (t in 1..comb){
            val registro  = Combinacion(null, null, null, null, null, null)
            combinaciones += registro
        }

        for (i in outfitList){
            Log.i("Registro: ", combinaciones.toString())
            if((i.categoria=="CAMISA M. LARGA" || i.categoria=="CAMISA M. CORTA"
                        || i.categoria=="CAMISETAS M. CORTA" || i.categoria=="CAMISETAS M. LARGA"
                        || i.categoria=="CAMISETAS TIRANTES" || i.categoria=="POLOS"
                        || i.categoria=="TOPS")) {
                var ctemp = 0
                for (registro in combinaciones){
                    if(registro.parteSuperior==null && ctemp<parteInferior*calzado){
                        registro.parteSuperior=i
                        ctemp++
                    }
                }
            }
            if ((i.categoria == "PANTALONES" || i.categoria == "PANTALONES CORTOS"
                        || i.categoria=="FALDAS" || i.categoria == "JEANS")){
                var ctemp = 0
                for (registro in combinaciones){
                    if(registro.parteInferior==null && ctemp<parteSuperior*calzado){
                        registro.parteInferior=i
                        ctemp++
                    }
                }
            }
            if((i.categoria == "DEPORTIVAS" || i.categoria == "ZAPATOS"
                        || i.categoria == "BOTAS Y BOTINES" || i.categoria == "SANDALIAS")){
                var ctemp = 0
                for (registro in combinaciones){
                    if(registro.calzado==null && ctemp<parteSuperior*parteInferior){
                        registro.calzado=i
                        ctemp++
                    }
                }
            }
            if ((i.categoria=="ABRIGOS" || i.categoria=="CAZADORAS" || i.categoria=="CHUBASQUERO"
                        || i.categoria=="BLAZERS") && prendasNecesarias>4) {
                var ctemp = 0
                for (registro in combinaciones){
                    if(registro.cazadoras==null && ctemp<parteSuperior*parteInferior*calzado*jerseis){
                        registro.cazadoras=i
                        ctemp++
                    }
                }
            }
            if ((i.categoria=="SOBRECAMISAS" || i.categoria=="CHALECOS" || i.categoria=="CHALECOS"
                        || i.categoria=="JERSÉIS" || i.categoria=="CHAQUETAS" || i.categoria=="SUDADERAS")
                && prendasNecesarias>3){
                var ctemp = 0
                for (registro in combinaciones){
                    if(registro.jerseis == null && ctemp<parteSuperior*parteInferior*calzado){
                        registro.jerseis=i
                        ctemp++
                    }
                }
            }
            if(i.categoria == "VESTIDOS" || i.categoria=="MONOS" || i.categoria=="TRAJES"){
                var ctemp = 0
                for (registro in combinaciones){
                    if(registro.conjuntos == null && ctemp<calzado){
                        registro.conjuntos=i
                        ctemp++
                    }
                }
            }
        }

        val indices: MutableList<Int> = mutableListOf()
        for((index, i) in combinaciones.withIndex()){
            if (i.calzado == null || i.parteInferior == null || i.parteSuperior==null){
                indices.add(0,index)
            }
        }

        for (i in indices){
            combinaciones.removeAt(i)
        }
        return combinaciones
    }

    private fun funFactorial(num: Int): Long {
        var factorial: Long=1
        for(i in 1..num){
            factorial*=i.toLong()
        }
        return factorial
    }

}