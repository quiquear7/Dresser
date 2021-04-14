package com.uc3m.dresser.ui.notifications

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentDashboardBinding
import com.uc3m.dresser.databinding.FragmentNotificationsBinding
import com.uc3m.dresser.viewModels.PrendaViewModel

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var auth: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)


        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        Picasso.with(context).load(currentUser.photoUrl)
            .into(binding.imageFoto)
        currentUser.photoUrl
        binding.textName.text = currentUser.displayName
        binding.textEmail.text = currentUser.email

        binding.bPrendas.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_notifications_to_listaFragment)
        }
        
        binding.bCalendar.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_notifications_to_historialFragment)
        }

        binding.cbLogout.setOnClickListener{
            auth.signOut()
            findNavController().navigate(R.id.action_navigation_notifications_to_authFragment)
        }
        return view
    }
}