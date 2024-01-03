package com.wasfan.fixfastbuddy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class HomeFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList : ArrayList<Int>
    private lateinit var vehicleList : ArrayList<String>
    private lateinit var adapter: VehicleImageAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var helplineList : ArrayList<Helpline>
    private lateinit var helplineAdapter : HelplineAdapter

    private lateinit var servicesRecyclerView: RecyclerView
    private lateinit var servicesList : ArrayList<ServicesDataClass>
    private lateinit var servicesAdapter : ServicesAdapterClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        init(view)
        setUpTransformer()
        viewPager2.setCurrentItem(1, false)

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                /*handler.postDelayed(runnable , 2000)*/
            }
        })

        return view
    }


    //Vehicle viewPager
    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(runnable)
    }

    /*override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable , 2000)
    }*/

    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    private fun setUpTransformer(){
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.2f
            page.scaleX = 0.85f + r * 0.2f
        }

        viewPager2.setPageTransformer(transformer)
    }

    private fun init(view: View){
        viewPager2 = view.findViewById(R.id.vehicleViewPager)
        handler = Handler(Looper.myLooper()!!)
        imageList = ArrayList()
        vehicleList = ArrayList()

        imageList.add(R.drawable.jeep)
        imageList.add(R.drawable.bmw)
        imageList.add(R.drawable.add_vehicle)

        vehicleList.add("Jeep Wrangler 2019")
        vehicleList.add("BMW 318i 2022")
        vehicleList.add("Add Vehicle")

        adapter = VehicleImageAdapter(requireContext(), imageList, vehicleList)
        adapter.setOnItemClickListener { position ->
            val intent = Intent(context, VehicleInfo::class.java)
            intent.putExtra("vehicleName", vehicleList[position])
            intent.putExtra("image", imageList[position])
            startActivity(intent)
        }

        viewPager2.adapter = adapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        //Helpline
        recyclerView = view.findViewById(R.id.helplineRecyclerViev)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext() , RecyclerView.HORIZONTAL , false)
        helplineList = ArrayList()

        helplineList.add(Helpline(R.drawable.hospital, "Hospital"))
        helplineList.add(Helpline(R.drawable.police, "Police"))
        helplineList.add(Helpline(R.drawable.fire_service, "Fire Service"))
        helplineList.add(Helpline(R.drawable.insurance, "Insurance"))

        helplineAdapter = HelplineAdapter(helplineList)
        recyclerView.adapter = helplineAdapter

        //Services
        servicesRecyclerView = view.findViewById(R.id.servicesRecyclerView)
        recyclerView.setHasFixedSize(true)
        servicesList = ArrayList()

        servicesList.add(ServicesDataClass(R.drawable.services_tyre, "Tyre"))
        servicesList.add(ServicesDataClass(R.drawable.services_engine, "Engine"))
        servicesList.add(ServicesDataClass(R.drawable.services_tow_truck, "Tow Truck"))
        servicesList.add(ServicesDataClass(R.drawable.services_battery, "Battery"))
        servicesList.add(ServicesDataClass(R.drawable.services_lockout, "Lockout"))
        servicesList.add(ServicesDataClass(R.drawable.services_fuel_delivery, "Fuel Delivery"))
        servicesList.add(ServicesDataClass(R.drawable.services_tyre, "Tyre"))
        servicesList.add(ServicesDataClass(R.drawable.services_engine, "Engine"))
        servicesList.add(ServicesDataClass(R.drawable.services_tow_truck, "Tow Truck"))
        servicesList.add(ServicesDataClass(R.drawable.services_battery, "Battery"))
        servicesList.add(ServicesDataClass(R.drawable.services_lockout, "Lockout"))
        servicesList.add(ServicesDataClass(R.drawable.services_fuel_delivery, "Fuel Delivery"))

        servicesRecyclerView.layoutManager = GridLayoutManager(requireContext() , 2, RecyclerView.HORIZONTAL , false)
        servicesAdapter = ServicesAdapterClass(servicesList)
        servicesRecyclerView.adapter = servicesAdapter

    }

}