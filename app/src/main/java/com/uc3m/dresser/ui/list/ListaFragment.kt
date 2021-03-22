package com.uc3m.dresser.ui.list

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uc3m.dresser.R
import com.uc3m.dresser.database.Prenda
import com.uc3m.dresser.database.Registro
import com.uc3m.dresser.databinding.FragmentListaBinding
import com.uc3m.dresser.ui.listadapter.ListaAdapter
import com.uc3m.dresser.ui.listadapter.SendPrenda
import com.uc3m.dresser.viewModels.PrendaViewModel


class ListaFragment : Fragment(), SendPrenda{
    private lateinit var binding: FragmentListaBinding
    private lateinit var prendaViewModel: PrendaViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapter = ListaAdapter(this)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration( DividerItemDecoration(context, LinearLayout.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter




        prendaViewModel = ViewModelProvider(this).get(PrendaViewModel::class.java)
        Log.i("información", "creación")
        prendaViewModel.readAll.observe(viewLifecycleOwner, { prenda ->
            if (prenda != null) {
                adapter.setData(prenda)
            }
        })


        return view
    }

    override fun sendInfo(prenda: Prenda, operacion: Int) {
        if(operacion==0){
            //edit
            setFragmentResult("envioPrenda", bundleOf("prenda" to prenda.id))
            findNavController().navigate(R.id.action_listaFragment_to_ropaFragment)
        }
        if(operacion==1){
            //delete
            val prendaViewModel = ViewModelProvider(this).get(PrendaViewModel::class.java)
            prendaViewModel.deletePrenda(prenda)
        }

    }


}