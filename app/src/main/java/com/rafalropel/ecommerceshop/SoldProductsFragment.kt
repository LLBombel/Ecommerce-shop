package com.rafalropel.ecommerceshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafalropel.ecommerceshop.adapter.SoldProductAdapter
import com.rafalropel.ecommerceshop.databinding.FragmentSoldProductsBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.SoldProduct


class SoldProductsFragment : Fragment() {

    private var _binding: FragmentSoldProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSoldProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getSoldProducts() {
        FireStoreClass().getSoldProducts(this)
    }

    fun successSoldProducts(soldProductsList: ArrayList<SoldProduct>) {
        if (soldProductsList.size > 0) {
            binding.rvSoldProductItems.visibility = View.VISIBLE
            binding.tvNoSoldProductsFound.visibility = View.GONE

            binding.rvSoldProductItems.layoutManager = LinearLayoutManager(activity)
            binding.rvSoldProductItems.setHasFixedSize(true)

            val soldProductsAdapter = SoldProductAdapter(requireActivity(), soldProductsList)
            binding.rvSoldProductItems.adapter = soldProductsAdapter
        } else {
            binding.rvSoldProductItems.visibility = View.GONE
            binding.tvNoSoldProductsFound.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        getSoldProducts()
        super.onResume()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}