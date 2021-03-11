package com.uc3m.dresser.ui.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.uc3m.dresser.databinding.FragmentListaBinding
import com.uc3m.dresser.ui.listadapter.ListaAdapter
import com.uc3m.dresser.viewModels.PrendaViewModel

class ListaFragment : Fragment() {
    private lateinit var binding: FragmentListaBinding
    private lateinit var prendaViewModel: PrendaViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListaBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapter = ListaAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        prendaViewModel = ViewModelProvider(this).get(PrendaViewModel::class.java)
        Log.i("información", "creación")
        prendaViewModel.readAll.observe(viewLifecycleOwner, { prenda->
            if (prenda != null){
                adapter.setData(prenda)
            }
        })


        return view
    }
}