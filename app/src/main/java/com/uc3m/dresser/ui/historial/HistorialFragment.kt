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
import androidx.recyclerview.widget.LinearLayoutManager
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentHistorialBinding
import com.uc3m.dresser.databinding.FragmentListaBinding
import com.uc3m.dresser.databinding.FragmentNotificationsBinding
import com.uc3m.dresser.ui.historialadapter.HistorialAdapter
import com.uc3m.dresser.ui.listadapter.ListaAdapter
import com.uc3m.dresser.ui.notifications.NotificationsViewModel
import com.uc3m.dresser.viewModels.PrendaViewModel
import java.text.SimpleDateFormat
import java.util.*

class HistorialFragment : Fragment() {

    private lateinit var binding: FragmentHistorialBinding
    private lateinit var prendaViewModel: PrendaViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentHistorialBinding.inflate(inflater, container, false)
        val view = binding.root
        prendaViewModel = ViewModelProvider(this).get(PrendaViewModel::class.java)

        binding.calendarView.date


        val adapter = HistorialAdapter()
        val recyclerView = binding.hRW
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        prendaViewModel.readDate(currentDate).observe(viewLifecycleOwner, { registro->
            adapter.deleteData()
            adapter.setData(registro)
        })


        binding.calendarView.setOnDateChangeListener{view, year, month, dayOfMonth ->
            val date = ""+dayOfMonth + "/" + (month + 1) + "/" + year
            val msg = "Selected date is $date"

            prendaViewModel.readDate(date).observe(viewLifecycleOwner, { registro->
                adapter.deleteData()
                Log.i("registro", registro.toString())
                adapter.setData(registro)
            })
            Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()

        }
        return view
    }
}