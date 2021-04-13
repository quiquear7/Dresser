package com.uc3m.dresser.ui.home

import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.uc3m.dresser.R
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Bienvenido"
    }
    val text: LiveData<String> = _text

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