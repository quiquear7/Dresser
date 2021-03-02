package com.uc3m.dresser.ui.notifications

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentDashboardBinding
import com.uc3m.dresser.databinding.FragmentNotificationsBinding
import com.uc3m.dresser.viewModels.PrendaViewModel

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var binding: FragmentNotificationsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val view = binding.root

        val texto = binding.aCategoria

        val imgFoto = binding.iRopa
        val prendaViewModel = ViewModelProvider(this).get(PrendaViewModel::class.java)
        prendaViewModel.readAll.observe(viewLifecycleOwner, {
            prenda -> for (item in prenda){
                texto.text = item.color +", "+item.id
                val imgBitmap: Bitmap =  BitmapFactory.decodeFile(item.ruta)
                imgFoto.setImageBitmap(imgBitmap)
            }
        })
        return view
    }
}