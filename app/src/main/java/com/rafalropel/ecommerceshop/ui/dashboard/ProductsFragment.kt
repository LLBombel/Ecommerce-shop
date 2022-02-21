package com.rafalropel.ecommerceshop.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafalropel.ecommerceshop.AddProductActivity
import com.rafalropel.ecommerceshop.R
import com.rafalropel.ecommerceshop.adapter.ProductsAdapter
import com.rafalropel.ecommerceshop.databinding.FragmentProductsBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Product

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)





        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getProductsListSuccess(productsList: ArrayList<Product>) {

        if (productsList.size > 0) {
            binding.rvProducts.visibility = View.VISIBLE
            binding.noProductsAdded.visibility = View.GONE

            binding.rvProducts.layoutManager = LinearLayoutManager(activity)
            binding.rvProducts.setHasFixedSize(true)
            val adapter = ProductsAdapter(requireActivity(), productsList)
            binding.rvProducts.adapter = adapter
        } else {
            binding.rvProducts.visibility = View.GONE
            binding.noProductsAdded.visibility = View.VISIBLE
        }
    }

    private fun getProductsListFromFirestore() {
        FireStoreClass().getProductsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductsListFromFirestore()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}