
package com.wasfan.fixfastbuddy2.mechanic
////from here
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import androidx.viewpager2.widget.ViewPager2
//import com.google.android.material.tabs.TabLayout
//
////
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.FragmentPagerAdapter
//import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
//import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
//import com.wasfan.fixfastbuddy2.R
//
//class ActivitiesFragment : Fragment() {
//
//    //new
//    private lateinit var tabLayout: TabLayout
//    private lateinit var viewPager2: ViewPager2
//    private lateinit var adapter: TaskStatusPageAdapter
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        tabLayout = findViewById(R.id.tabLayout)
//        viewPager2 = findViewById(R.id.viewPager2)
//
//        adapter = TaskStatusPageAdapter(supportFragmentManager,lifecycle)
//
//        tabLayout.addTab(tabLayout.newTab().setText("Ongoing"))
//        tabLayout.addTab(tabLayout.newTab().setText("Completed"))
//        tabLayout.addTab(tabLayout.newTab().setText("Cancelled"))
//
//        viewPager2.adapter = adapter
//
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                if (tab != null) {
//                    viewPager2.currentItem = tab.position
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//
//            }
//
//        })
//
//        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                tabLayout.selectTab(tabLayout.getTabAt(position))
//            }
//        })
//    }
//
//
////    private lateinit var tabLayout: TabLayout
////    private lateinit var viewPager2: ViewPager2
////    private lateinit var adapter: ActivitiesFragmentPageAdapter
////
////    override fun onCreateView(
////        inflater: LayoutInflater, container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        // Inflate the layout for this fragment
////        val view = inflater.inflate(R.layout.fragment_activities, container, false)
////
////        tabLayout = view.findViewById(R.id.activities_tab)
////        viewPager2 = view.findViewById(R.id.viewpager_activities)
////
////        adapter = ActivitiesFragmentPageAdapter(childFragmentManager,lifecycle
////        )
////        viewPager2.adapter = adapter
////
////
////        tabLayout.addTab(tabLayout.newTab().setText("Ongoing"))
////        tabLayout.addTab(tabLayout.newTab().setText("Completed"))
////        tabLayout.addTab(tabLayout.newTab().setText("Cancelled"))
////
////        // Set the default tab to be "Ongoing"
////        tabLayout.getTabAt(0)?.select()
////
////        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
////            override fun onTabSelected(tab: TabLayout.Tab?) {
////                if (tab != null) {
////                    viewPager2.currentItem = tab.position
////                }
////            }
////
////            override fun onTabUnselected(tab: TabLayout.Tab?) {
////
////            }
////
////            override fun onTabReselected(tab: TabLayout.Tab?) {
////
////            }
////
////        })
////
////        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
////            override fun onPageSelected(position: Int) {
////                super.onPageSelected(position)
////                tabLayout.selectTab(tabLayout.getTabAt(position))
////            }
////        })
////
////
////        return view
////    }
//
//}
////to here

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.wasfan.fixfastbuddy2.R

class ActivitiesFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: TaskStatusPageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_activities, container, false)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager2 = view.findViewById(R.id.viewPager2)

        adapter = TaskStatusPageAdapter(childFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("Ongoing"))
        tabLayout.addTab(tabLayout.newTab().setText("Completed"))
        tabLayout.addTab(tabLayout.newTab().setText("Cancelled"))

        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        return view
    }
}