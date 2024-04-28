package com.wasfan.fixfastbuddy

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.wasfan.fixfastbuddy.adapterClasses.ServicesAdapterClass
import com.wasfan.fixfastbuddy.dataClasses.ServicesDataClass
import com.wasfan.fixfastbuddy.dataClasses.Vehicles
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class HomeFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var vehicleList: ArrayList<String>
    private lateinit var licensePlateNoList: ArrayList<String>
    private lateinit var vehicleIdList: ArrayList<String>
    private lateinit var adapter: VehicleImageAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var helplineList: ArrayList<Helpline>
    private lateinit var helplineAdapter: HelplineAdapter

    private lateinit var servicesRecyclerView: RecyclerView
    private lateinit var servicesList: ArrayList<ServicesDataClass>
    private lateinit var servicesAdapter: ServicesAdapterClass

    private lateinit var homeUserName: TextView
    private val currentUser = FirebaseAuth.getInstance().currentUser
    val phoneNumber = currentUser?.phoneNumber.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        init(view)
        getUserInfo(phoneNumber)
        viewPager2()
        servicesRecyclerView()
        helplineRecyclerview()
        setUpTransformer()

        return view
    }

    private fun getUserInfo(phoneNumber: String) {
        val apiService = RetrofitInstance.api

        val call = apiService.getUserDetails(phoneNumber)
        call.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        homeUserName.text = "Hello, ${user.firstName}"
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch user details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun viewPager2() {
        val apiService = RetrofitInstance.api

        val call: Call<List<Vehicles>> = apiService.getVehicleDetails(phoneNumber)

        call.enqueue(object : Callback<List<Vehicles>> {
            override fun onResponse(
                call: Call<List<Vehicles>>,
                response: Response<List<Vehicles>>
            ) {
                if (response.isSuccessful) {
                    val vehicleListDB: List<Vehicles>? = response.body()
                    // Initialize imageList and vehicleList here
                    imageList = ArrayList()
                    vehicleList = ArrayList()
                    licensePlateNoList = ArrayList()
                    vehicleIdList = ArrayList()

                    if (vehicleListDB != null) {
                        for (vehicle in vehicleListDB) {
                            vehicleList.add("${vehicle.make} ${vehicle.model} ${vehicle.year}")
                            imageList.add(R.drawable.bmw)
                            licensePlateNoList.add(vehicle.licensePlateNo)
                            vehicleIdList.add(vehicle.vehicleId.toString())
                        }
                    }

                    // Add the "Add Vehicle" item
                    vehicleList.add("Add Vehicle")
                    imageList.add(R.drawable.add_vehicle)

                    // Initialize the handler and adapter here
                    handler = Handler(Looper.myLooper()!!)
                    adapter = VehicleImageAdapter(requireContext(), imageList, vehicleList)
                    adapter.setOnItemClickListener(object :
                        VehicleImageAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            if (position == imageList.size - 1) {
                                val intent = Intent(context, AddNewVehicle::class.java)
                                startActivity(intent)
                            } else {
                                val intent = Intent(context, VehicleInfo::class.java)
                                intent.putExtra("position", position)
                                startActivity(intent)
                            }
                        }
                    })

                    // Set up the ViewPager2 with the initialized adapter
                    viewPager2.adapter = adapter
                    viewPager2.offscreenPageLimit = 3
                    viewPager2.clipToPadding = false
                    viewPager2.clipChildren = false
                    viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

                    if (vehicleList.size >= 3) {
                        viewPager2.setCurrentItem(1, false)
                    }

                    viewPager2.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            handler.removeCallbacks(runnable)
                            /*handler.postDelayed(runnable , 2000)*/
                        }
                    })
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<List<Vehicles>>, t: Throwable) {
                // Handle network error

                // Initialize the handler and adapter with default values
                handler = Handler(Looper.myLooper()!!)
                imageList = ArrayList()
                vehicleList = ArrayList()
                vehicleList.add("Add Vehicle")
                imageList.add(R.drawable.add_vehicle)

                adapter = VehicleImageAdapter(requireContext(), imageList, vehicleList)
                adapter.setOnItemClickListener(object : VehicleImageAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        if (position == imageList.size - 1) {
                            val intent = Intent(context, AddNewVehicle::class.java)
                            startActivity(intent)
                        }
                    }
                })
                viewPager2.adapter = adapter

            }
        })

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

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.2f
            page.scaleX = 0.85f + r * 0.2f
        }

        viewPager2.setPageTransformer(transformer)
    }
    //ViewPager2 End

    private fun helplineRecyclerview() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        helplineList = ArrayList()

        helplineList.add(Helpline(R.drawable.hospital, "Hospital", "+9411-2691111"))
        helplineList.add(Helpline(R.drawable.police, "Police", "119"))
        helplineList.add(Helpline(R.drawable.fire_service, "Fire Service", "110"))
        helplineList.add(Helpline(R.drawable.insurance, "Insurance", "+941234"))

        helplineAdapter = HelplineAdapter(helplineList)
        recyclerView.adapter = helplineAdapter

        helplineAdapter.setOnItemClickListener(object : HelplineAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                showHelplinePopup(
                    helplineList[position].helplineImage,
                    helplineList[position].helplineName,
                    helplineList[position].helplinePhoneNumber
                )
            }
        })
    }

    private fun showHelplinePopup(image: Int, text: String, phoneNumber: String) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.helpline_popup_layout, null)
        builder.setView(dialogView)

        val alertDialog = builder.create()

        dialogView.findViewById<ImageView>(R.id.helplineImage).setImageResource(image)
        dialogView.findViewById<TextView>(R.id.dialogTitle).text = "Call $text"
        dialogView.findViewById<TextView>(R.id.dialogMessage).text =
            "Are you sure you want to call the $text"

        dialogView.findViewById<Button>(R.id.btnCall).setOnClickListener {
            // Call helpline
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            startActivity(intent)
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun towingPopUp(position: Int) {
        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.setContentView(R.layout.towing_popup_layout)
        dialogView.window!!.setBackgroundDrawable(requireContext().getDrawable(R.drawable._10dp_border_radius_bg))

        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)
        val nextButton = dialogView.findViewById<Button>(R.id.nextBtn)

        nextButton.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId // Get the selected radio button ID
            if (selectedId != -1) { // Check if a selection was made
                val selectedRadioButton = dialogView.findViewById<RadioButton>(selectedId)
                val selection = selectedRadioButton.text.toString()
                val intent = Intent(context, MapsActivity::class.java)
                intent.putExtra("vehicleId", vehicleIdList[viewPager2.currentItem])
                intent.putExtra("vehicleName", vehicleList[viewPager2.currentItem])
                intent.putExtra("vehicleImage", imageList[viewPager2.currentItem])
                intent.putExtra("licensePlateNo", licensePlateNoList[viewPager2.currentItem])
                intent.putExtra("serviceName", Services.servicesList[position].servicesName)
                intent.putExtra("serviceImage", Services.servicesList[position].servicesImage)
                intent.putExtra("serviceId", Services.servicesList[position].serviceId)
                intent.putExtra("description", selection)
                startActivity(intent)

                dialogView.dismiss()
            }
        }

        dialogView.findViewById<ImageButton>(R.id.CloseButton).setOnClickListener {
            dialogView.dismiss()
        }

        dialogView.show()
    }

    private fun fuelDeliveryPopUp(position: Int) {
        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.setContentView(R.layout.fuel_delivery_popup)
        dialogView.window!!.setBackgroundDrawable(requireContext().getDrawable(R.drawable._10dp_border_radius_bg))

        val fuelAmountET = dialogView.findViewById<EditText>(R.id.fuelAmount)

        val fuelSpinner: Spinner = dialogView.findViewById(R.id.fuelSpinner)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("Select Fuel Type", "Petrol", "Diesel")
        )
        fuelSpinner.adapter = adapter

        val description = "${fuelSpinner.selectedItem} - ${fuelAmountET.text} litres"

        dialogView.findViewById<Button>(R.id.nextBtn).setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra("vehicleId", vehicleIdList[viewPager2.currentItem])
            intent.putExtra("vehicleName", vehicleList[viewPager2.currentItem])
            intent.putExtra("vehicleImage", imageList[viewPager2.currentItem])
            intent.putExtra("licensePlateNo", licensePlateNoList[viewPager2.currentItem])
            intent.putExtra("serviceName", Services.servicesList[position].servicesName)
            intent.putExtra("serviceImage", Services.servicesList[position].servicesImage)
            intent.putExtra("serviceId", Services.servicesList[position].serviceId)
            intent.putExtra("description", description)
            startActivity(intent)

            dialogView.dismiss()
        }

        dialogView.findViewById<ImageButton>(R.id.CloseButton).setOnClickListener {
            dialogView.dismiss()
        }

        dialogView.show()
    }

    private fun servicesRecyclerView() {
        servicesRecyclerView.setHasFixedSize(true)

        servicesList = Services.servicesList

        servicesRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
        servicesAdapter = ServicesAdapterClass(servicesList)
        servicesRecyclerView.adapter = servicesAdapter
        servicesAdapter.setOnItemClickListener(object : ServicesAdapterClass.onItemClickListener {
            override fun onItemClick(position: Int) {
               // if (Manager.onGoing) {
                    if (position == 2) {
                        towingPopUp(position)
                    } else if (position == 5) {
                        fuelDeliveryPopUp(position)
                    } else {
                        val intent = Intent(context, MapsActivity::class.java)
                        intent.putExtra("vehicleId", vehicleIdList[viewPager2.currentItem])
                        intent.putExtra("vehicleName", vehicleList[viewPager2.currentItem])
                        intent.putExtra("vehicleImage", imageList[viewPager2.currentItem])
                        intent.putExtra(
                            "licensePlateNo",
                            licensePlateNoList[viewPager2.currentItem]
                        )
                        intent.putExtra("serviceName", Services.servicesList[position].servicesName)
                        intent.putExtra(
                            "serviceImage",
                            Services.servicesList[position].servicesImage
                        )
                        intent.putExtra("serviceId", Services.servicesList[position].serviceId)
                        startActivity(intent)
                    }
                /*}else{
                    Toast.makeText(requireContext(), "There is already a service ongoing.", Toast.LENGTH_LONG).show()
                }*/
            }
        })
    }

    private fun init(view: View) {
        viewPager2 = view.findViewById(R.id.vehicleViewPager)
        recyclerView = view.findViewById(R.id.helplineRecyclerViev)
        servicesRecyclerView = view.findViewById(R.id.servicesRecyclerView)
        homeUserName = view.findViewById(R.id.homeUserName)
    }

}