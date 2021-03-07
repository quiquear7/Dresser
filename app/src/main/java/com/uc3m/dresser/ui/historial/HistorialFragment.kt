package com.uc3m.dresser.ui.historial

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentHistorialBinding
import com.uc3m.dresser.databinding.FragmentNotificationsBinding
import com.uc3m.dresser.ui.notifications.NotificationsViewModel

class HistorialFragment : Fragment() {

    private lateinit var binding: FragmentHistorialBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentHistorialBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.calendarView.date

        binding.calendarView.setOnDateChangeListener{view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
        }

        return view
    }
}