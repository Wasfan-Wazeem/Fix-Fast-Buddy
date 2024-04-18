package com.wasfan.fixfastbuddy2.mechanic

//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//
//
////private const val ARG_PARAM1 = "param1"
////private const val ARG_PARAM2 = "param2"
//
//
//
//class cancelled : Fragment() {
//    // TODO: Rename and change types of parameters
////    private var param1: String? = null
////    private var param2: String? = null
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        arguments?.let {
////            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
////        }
////    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_cancelled, container, false)
//    }
//}
//
//
////private fun Bundle.getString(argParam1: String?): String? {}

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy2.mechanic.DataClass.CancelledTask
import com.wasfan.fixfastbuddy2.mechanic.AdapterClass.CancelledTaskAdapter
import com.wasfan.fixfastbuddy2.R


//new 2 lines
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class cancelled : Fragment() {

//    private lateinit var recyclerView: RecyclerView
//    private lateinit var dataList: ArrayList<CompletedDataClass>
//    private lateinit var imageList: Array<Int>
//    private lateinit var serviceID: Array<String>
//    private lateinit var serviceType: Array<String>
//    private lateinit var vehicleNum: Array<String>
//    private lateinit var address: Array<String>
//    private lateinit var date: Array<String>
//    private lateinit var time: Array<String>
//    private lateinit var completedAmount: Array<String>
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_cancelled, container, false)
//
//        initLists()
//
//        recyclerView = view.findViewById(R.id.recycler_cancelled)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.setHasFixedSize(true)
//
//        dataList = arrayListOf<CompletedDataClass>()
//        getData()
//
//        return view
//    }
//
//    private fun getData(){
//        for(i in imageList.indices){
//            val dataClass = CompletedDataClass(imageList[i], serviceID[i], serviceType[i], vehicleNum[i], address[i], date[i], time[i], completedAmount[i])
//            dataList.add(dataClass)
//        }
//        recyclerView.adapter = CancelledAdapterClass(dataList)
//    }
//
//    private fun initLists(){
//        imageList = arrayOf(
//            R.drawable.police,
//            R.drawable.service_icon
//        )
//
//        serviceID = arrayOf(
//            "S15518209",
//            "S16251325"
//        )
//
//        serviceType = arrayOf(
//            "Tyre",
//            "Engine"
//        )
//
//        vehicleNum = arrayOf(
//            "CBH-0484",
//            "CAC-0282"
//        )
//
//        address = arrayOf(
//            "123-131 Philip Gunawardena Mawatha, Colombo 00400",
//            "252 2/1 Matale Road Katugastota"
//        )
//
//        date = arrayOf(
//            "2023-10-21",
//            "2024-01-01"
//        )
//
//        time = arrayOf(
//            "17:23:29",
//            "20:16:47"
//        )
//
//        completedAmount = arrayOf(
//            "LKR 0.00",
//            "LKR 450.00"
//        )
//    }
//

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adapter: CancelledTaskAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_cancelled, container, false)
        recyclerView = view.findViewById(R.id.rvCancelled)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val cancelledTask = listOf(
            CancelledTask("Alex", "01/01/2023", "Tire","User Ask to Cancelled"),
            CancelledTask("Mathew", "02/01/2023", "Fuel","User Ask to Cancelled"),
            CancelledTask("Dasuni", "03/01/2023", "Engine","User Ask to Cancelled")
        )

        adapter = CancelledTaskAdapter(cancelledTask,requireFragmentManager())
        recyclerView.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CancelledFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            cancelled().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
