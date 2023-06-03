package com.example.project_1.fragments.delivery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.project_1.R
import com.example.project_1.adapters.DeliveryTabsPagerAdapter
import com.example.project_1.adapters.RestaurantTabsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DeliveryOrdersFragment : Fragment() {

    var myView : View? = null

    var viewPager: ViewPager2? = null
    var tabLayout: TabLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_delivery_orders, container, false)

        viewPager = myView?.findViewById(R.id.viewPager)
        tabLayout = myView?.findViewById(R.id.tabLayout)

        //tabLayout?.setSelectedTabIndicator(Color.BLACK)
        tabLayout?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        tabLayout?.tabTextColors = ContextCompat.getColorStateList(requireContext(), R.color.black)
        tabLayout?.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout?.isInlineLabel = true


        val numberOfTab = 4
        val adapter = DeliveryTabsPagerAdapter(requireActivity().supportFragmentManager, lifecycle, numberOfTab)
        viewPager?.adapter = adapter
        viewPager?.isUserInputEnabled = true

        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            when(position) {
                0 -> {
                    tab.text = "DESPACHADO"
                }
                1 -> {
                    tab.text = "EN CAMINO"
                }
                2 -> {
                    tab.text = "ENTREGADO"
                }
            }
        }.attach()

        return myView
    }
}