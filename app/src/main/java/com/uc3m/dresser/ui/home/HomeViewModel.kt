package com.uc3m.dresser.ui.home

import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.uc3m.dresser.R
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class HomeViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()
    private val _text = MutableLiveData<String>().apply {
        value = "Bienvenido"
    }
    val text: LiveData<String> = _text

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