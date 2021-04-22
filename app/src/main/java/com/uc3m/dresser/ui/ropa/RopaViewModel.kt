package com.uc3m.dresser.ui.ropa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class RopaViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()
    private val _text = MutableLiveData<String>().apply {
        value = "Agregar Nueva Prenda de Ropa"
    }
    val text: LiveData<String> = _text

    fun checkKey(): Boolean{
        val keystore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)
        val secretKeyEntry = keystore.getEntry(auth.currentUser.email, null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey != null
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


}