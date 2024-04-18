package com.wasfan.fixfastbuddy2.mechanic

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter



    class ActivitiesFragmentPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
        : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ongoing()
                1 -> completed()
                2 -> cancelled()
                else -> throw IllegalArgumentException("Invalid position")
            }
        }


    }


