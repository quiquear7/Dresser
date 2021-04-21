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

    fun checkKey(): Boolean {
        val keystore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)
        val secretKeyEntry = keystore.getEntry("MyKeyStore", null) as? KeyStore.SecretKeyEntry
        return if (secretKeyEntry != null) {
            secretKeyEntry.secretKey != null
        } else {
            false
        }
    }

    fun getKey(): SecretKey {
        val keystore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)
        val secretKeyEntry = keystore.getEntry("MyKeyStore", null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }

    fun encrypData(data: String): Pair<ByteArray, ByteArray> {
        val cipher: Cipher = Cipher.getInstance("AES/CBC/NoPadding")
        var temp: String = data
        while (temp.toByteArray().size % 16 != 0)
            temp += "\u0020"
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val ivBytes = cipher.iv
        val encryptedBytes = cipher.doFinal(temp.toByteArray())
        return Pair(ivBytes, encryptedBytes)

    }

    fun decryptData(ivBytes: ByteArray, data: ByteArray): String {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val spec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)
        return cipher.doFinal(data).toString(Charsets.UTF_8).trim()
    }


    fun generarOutfits(outfitList: List<Prenda>, temperatura: Float, llueve: Boolean): List<Combinacion> {

        val combinaciones = emptyList<Combinacion>().toMutableList()
        var prendasNecesarias = 5

        if (temperatura > 15 && temperatura <= 25) {
            prendasNecesarias = 4
        }
        if (temperatura > 25) {
            prendasNecesarias = 3
        }

        val parteSuperiorL = emptyList<Prenda>().toMutableList()
        val parteInferiorL = emptyList<Prenda>().toMutableList()
        val calzadoL = emptyList<Prenda>().toMutableList()
        val jerseisL = emptyList<Prenda>().toMutableList()
        val cazadorasL = emptyList<Prenda>().toMutableList()

        var parteSuperior = 0
        var parteInferior = 0
        var calzado = 0
        var jerseis = 0
        var cazadoras = 0


        for (i in outfitList) {
            if ((i.categoria == "CAMISA M. LARGA" || i.categoria == "CAMISA M. CORTA"
                            || i.categoria == "CAMISETAS M. CORTA" || i.categoria == "CAMISETAS M. LARGA"
                            || i.categoria == "CAMISETAS TIRANTES" || i.categoria == "POLOS"
                            || i.categoria == "TOPS")) {
                parteSuperior++
                parteSuperiorL.add(i)
            }
            if ((i.categoria == "PANTALONES" || i.categoria == "PANTALONES CORTOS"
                            || i.categoria == "FALDAS" || i.categoria == "JEANS")) {
                parteInferior++
                parteInferiorL.add(i)
            }
            if ((i.categoria == "DEPORTIVAS" || i.categoria == "ZAPATOS"
                            || i.categoria == "BOTAS Y BOTINES" || i.categoria == "SANDALIAS")) {
                calzado++
                calzadoL.add(i)
            }
            if ((i.categoria == "ABRIGOS" || i.categoria == "CAZADORAS" || i.categoria == "CHUBASQUERO"
                            || i.categoria == "BLAZERS")) {
                cazadoras++
                cazadorasL.add(i)

            }
            if ((i.categoria == "SOBRECAMISAS" || i.categoria == "CHALECOS" || i.categoria == "CHALECOS"
                            || i.categoria == "JERSÉIS" || i.categoria == "CHAQUETAS" || i.categoria == "SUDADERAS")) {
                jerseis++
                jerseisL.add(i)
            }

        }
        var comb = 1
        if (parteSuperior > 0) {
            comb *= parteSuperior
        }
        if (parteInferior > 0) {
            comb *= parteInferior
        }
        if (calzado > 0) {
            comb *= calzado
        }
        if (jerseis > 0) {
            comb *= jerseis
        }
        if (cazadoras > 0) {
            comb *= cazadoras
        }


        for (t in 1..comb) {
            val registro = Combinacion(null, null, null, null, null, null)
            combinaciones += registro
        }

        var indexCazadoras = 0
        var ctempCazadoras = 0
        var indexJerseis = 0
        var ctempJerseis = 0
        var indexPSuperior = 0
        var ctempPSuperior = 0
        var indexPInferior = 0
        var ctempPInferior = 0
        var indexCalzado = 0
        var ctempCalzado = 0

        var sCazadoras = comb
        if (cazadoras > 0) {
            sCazadoras = comb / cazadoras
        }
        var sJerseis = sCazadoras
        if (jerseis > 0) {
            sJerseis = sCazadoras / jerseis
        }
        var sParteSuperior = sJerseis
        if (parteSuperior > 0) {
            sParteSuperior = sJerseis / parteSuperior
        }
        var sParteInferior = sParteSuperior
        if (parteInferior > 0) {
            sParteInferior = sParteSuperior / parteInferior
        }
        var sCalzado = sParteInferior
        if (calzado > 0) {
            sCalzado = sParteInferior / calzado
        }


        for (registro in combinaciones) {

            if (prendasNecesarias == 5 && cazadoras > 0) {
                if (registro.cazadoras == null && nPrendas(registro) < prendasNecesarias) {
                    if (ctempCazadoras < sCazadoras) {
                        registro.cazadoras = cazadorasL[indexCazadoras]
                        ctempCazadoras++
                    } else {
                        if (indexCazadoras + 1 == cazadoras) {
                            indexCazadoras = 0
                        } else {
                            indexCazadoras++
                        }
                        registro.cazadoras = cazadorasL[indexCazadoras]
                        ctempCazadoras = 1
                    }
                }
            }

            if (prendasNecesarias == 4 && jerseis > 0) {
                if (registro.jerseis == null && nPrendas(registro) < prendasNecesarias) {
                    if (ctempJerseis < sJerseis) {
                        registro.jerseis = jerseisL[indexJerseis]
                        ctempJerseis++
                    } else {
                        if (indexJerseis + 1 == jerseis) {
                            indexJerseis = 0
                        } else {
                            indexJerseis++
                        }
                        registro.jerseis = jerseisL[indexJerseis]
                        ctempJerseis = 1
                    }
                }
            }

            if (parteSuperior > 0) {
                if (registro.parteSuperior == null && nPrendas(registro) < prendasNecesarias) {
                    if (ctempPSuperior < sParteSuperior) {
                        registro.parteSuperior = parteSuperiorL[indexPSuperior]
                        ctempPSuperior++
                    } else {
                        if (indexPSuperior + 1 == parteSuperior) {
                            indexPSuperior = 0
                        } else {
                            indexPSuperior++
                        }
                        registro.parteSuperior = parteSuperiorL[indexPSuperior]
                        ctempPSuperior = 1
                    }
                }
            }

            if (parteInferior > 0) {
                if (registro.parteInferior == null && nPrendas(registro) < prendasNecesarias) {
                    if (ctempPInferior < sParteInferior) {
                        registro.parteInferior = parteInferiorL[indexPInferior]
                        ctempPInferior++
                    } else {
                        if (indexPInferior + 1 == parteInferior) {
                            indexPInferior = 0
                        } else {
                            indexPInferior++
                        }
                        registro.parteInferior = parteInferiorL[indexPInferior]
                        ctempPInferior = 1
                    }
                }
            }

            if (calzado > 0) {
                if (registro.calzado == null && nPrendas(registro) < prendasNecesarias) {
                    if (ctempCalzado < sCalzado) {
                        registro.calzado = calzadoL[indexCalzado]
                        ctempCalzado++
                    } else {
                        if (indexCalzado + 1 == calzado) {
                            indexCalzado = 0
                        } else {
                            indexCalzado++
                        }
                        registro.calzado = calzadoL[indexCalzado]
                        ctempCalzado = 1
                    }
                }
            }
        }

        val indices: MutableList<Int> = mutableListOf()
        for ((index, i) in combinaciones.withIndex()) {
            if (i.calzado == null || nPrendas(i) < prendasNecesarias) {
                indices.add(0, index)
            }else{
               if ((i.parteInferior?.color  == "MARRÓN" && i.parteSuperior?.color  == "NEGRO")
                       || (i.parteInferior?.color  == "NEGRO" && i.parteSuperior?.color  == "MARRÓN")
                       || (i.parteInferior?.color  == "AZUL" && i.parteSuperior?.color  == "NEGRO")
                       || (i.parteInferior?.color  == "NEGRO" && i.parteSuperior?.color  == "AZUL")
                       || (i.parteInferior?.color  == "ROJO" && i.parteSuperior?.color  == "VERDE")
                       || (i.parteInferior?.color  == "VERDE" && i.parteSuperior?.color  == "ROJO")
                       || (i.parteInferior?.color  == "ROSA" && i.parteSuperior?.color  == "VERDE")
                       || (i.parteInferior?.color  == "VERDE" && i.parteSuperior?.color  == "ROSA")
                       || (i.parteInferior?.color  == "NARANJA" && i.parteSuperior?.color  == "VERDE")
                       || (i.parteInferior?.color  == "VERDE" && i.parteSuperior?.color  == "NARANJA")
                       || (i.parteInferior?.color  == "MORADO" && i.parteSuperior?.color  == "AMARILLO")
                       || (i.parteInferior?.color  == "AMARILLO" && i.parteSuperior?.color  == "MORADO")
                       || (i.parteInferior?.color  == "ROJO" && i.parteSuperior?.color  == "NARANJA")
                       || (i.parteInferior?.color  == "NARANJA" && i.parteSuperior?.color  == "ROJO")
                       || (i.parteInferior?.estampado  == "RAYAS" && i.parteSuperior?.estampado  == "CUADROS")
                       || (i.parteInferior?.estampado  == "CUADROS" && i.parteSuperior?.estampado  == "RAYAS")
                       || (prendasNecesarias==3 && (i.parteSuperior?.categoria  == "CAMISA M. LARGA"
                               || i.parteSuperior?.categoria  == "CAMISETAS M. LARGA"  ))
                       ){
                   indices.add(0, index)
                }
            }
        }

        for (i in indices) {
            combinaciones.removeAt(i)
        }
        return combinaciones
    }


    private fun nPrendas(registro: Combinacion): Int {
        var cont = 0
        if (registro.parteSuperior != null) cont++
        if (registro.parteInferior != null) cont++
        if (registro.conjuntos != null) cont += 2
        if (registro.cazadoras != null) cont++
        if (registro.calzado != null) cont++
        if (registro.jerseis != null) cont++
        return cont
    }

}