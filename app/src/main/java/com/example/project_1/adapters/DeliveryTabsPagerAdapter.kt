package com.example.project_1.adapters

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_1.fragments.delivery.DeliveryOrdersStatusFragment

class DeliveryTabsPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var numberOfTabs: Int
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return numberOfTabs
    }

    override fun createFragment(position: Int) : Fragment {
        Log.i("TAB_PAGER_ADAPTER", "createFragment: $position")
        when(position) {
            0 -> {
                val bundle = Bundle()
                bundle.putString("status", "DESPACHADO")
                val fragment = DeliveryOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("status", "EN CAMINO")
                val fragment = DeliveryOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString("status", "ENTREGADO")
                val fragment = DeliveryOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            else -> return DeliveryOrdersStatusFragment()
        }
    }
}