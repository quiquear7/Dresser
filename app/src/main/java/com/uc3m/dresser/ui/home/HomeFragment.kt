package com.uc3m.dresser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentDashboardBinding
import com.uc3m.dresser.databinding.FragmentHomeBinding
import com.uc3m.dresser.repository.Repository
import com.uc3m.dresser.viewModels.MainViewModel
import com.uc3m.dresser.viewModels.MainViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getClima()

        /*viewModel.myResponse.observe(this, { response ->
            if(response.isSuccessful){
               //val clima = response.body()?.getClima.lasts
            }
            else{

            }
        })*/


        return view
    }


}