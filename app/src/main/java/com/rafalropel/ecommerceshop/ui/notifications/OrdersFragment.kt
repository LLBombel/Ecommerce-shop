package com.rafalropel.ecommerceshop.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafalropel.ecommerceshop.adapter.OrdersAdapter
import com.rafalropel.ecommerceshop.databinding.FragmentOrdersBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Order

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }


    fun populateOrdersList(ordersList: ArrayList<Order>){
        if(ordersList.size > 0){
            binding.tvNoOrdersFound.visibility = View.GONE
            binding.rvMyOrderItems.visibility = View.VISIBLE

            binding.rvMyOrderItems.layoutManager = LinearLayoutManager(activity)
            binding.rvMyOrderItems.setHasFixedSize(true)

            val ordersAdapter = OrdersAdapter(requireActivity(), ordersList)
            binding.rvMyOrderItems.adapter = ordersAdapter

        }else{
            binding.tvNoOrdersFound.visibility = View.VISIBLE
            binding.rvMyOrderItems.visibility = View.GONE
        }
    }

    private fun getOrdersList(){
        FireStoreClass().getOrdersList(this@OrdersFragment)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        getOrdersList()
        super.onResume()
    }
}