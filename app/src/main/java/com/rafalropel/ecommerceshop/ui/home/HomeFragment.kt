package com.rafalropel.ecommerceshop.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.rafalropel.ecommerceshop.CartListActivity
import com.rafalropel.ecommerceshop.ProductDetailsActivity
import com.rafalropel.ecommerceshop.R
import com.rafalropel.ecommerceshop.SettingsActivity
import com.rafalropel.ecommerceshop.adapter.ProductHomeAdapter
import com.rafalropel.ecommerceshop.databinding.FragmentHomeBinding
import com.rafalropel.ecommerceshop.firestore.FireStoreClass
import com.rafalropel.ecommerceshop.model.Product
import com.rafalropel.ecommerceshop.utils.Constants

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

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
        //val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onResume() {
        super.onResume()
        getHomeItemsList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            R.id.action_cart ->{
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getHomeItemsSuccess(homeItemsList: ArrayList<Product>) {
        if (homeItemsList.size > 0) {
            binding.tvNoItemsHome.visibility = View.GONE
            binding.rvHome.visibility = View.VISIBLE

            binding.rvHome.layoutManager = GridLayoutManager(activity, 2)
            binding.rvHome.setHasFixedSize(true)
            val adapter = ProductHomeAdapter(requireActivity(), homeItemsList)
            binding.rvHome.adapter = adapter

            adapter.setOnClickListener(object: ProductHomeAdapter.OnClickListener{
                override fun onClick(position: Int, product: Product) {
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.product_id)
                    intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, product.user_id)
                    startActivity(intent)
                }
            } )
        } else {
            binding.tvNoItemsHome.visibility = View.VISIBLE
            binding.rvHome.visibility = View.GONE
        }
    }

    private fun getHomeItemsList() {
        FireStoreClass().getHomeItemsList(this@HomeFragment)
    }
}