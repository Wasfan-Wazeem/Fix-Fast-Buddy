//package com.wasfan.fixfastbuddy2.mechanic
//
//import android.os.Bundle
//import android.widget.ImageButton
//import androidx.appcompat.app.AppCompatActivity
//import com.wasfan.fixfastbuddy2.R
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.wasfan.fixfastbuddy2.mechanic.AdapterClass.EarningsAdapter
//import com.wasfan.fixfastbuddy2.mechanic.DataClass.DataClassEarnings
//
//class Earnings : AppCompatActivity() {
//    private lateinit var backBtn: ImageButton
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var dataList: ArrayList<DataClassEarnings>
//    lateinit var titleList: Array<String>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_earnings)
//
//        backBtn = findViewById(R.id.backBtn)
//        backBtn.setOnClickListener {
//            onBackPressed()
//
//            titleList = arrayOf(
//                "Tire",
//                "Fuel",
//                "ImageView"
//            )
//
//
//            recyclerView = findViewById(R.id.mngservices_rv)
//            recyclerView.layoutManager = LinearLayoutManager(this)
//            recyclerView.setHasFixedSize(true)
//
//            dataList = arrayListOf<DataClassEarnings>()
//            getData()
//        }
//        private fun getData() {
//            for (i in titleList.indices) {
//                val dataClass = DataClassEarnings(titleList[i])
//                dataList.add(dataClass)
//            }
//            recyclerView.adapter = EarningsAdapter(dataList)
//
//        }
//    }
//}

package com.wasfan.fixfastbuddy2.mechanic
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.R.id.earning_rv
import com.wasfan.fixfastbuddy2.mechanic.AdapterClass.EarningsAdapter
import com.wasfan.fixfastbuddy2.mechanic.DataClass.DataClassEarnings

class Earnings : AppCompatActivity() {

    private lateinit var backBtn: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClassEarnings>
    lateinit var date:Array<String>
    lateinit var amount:Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)


        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()}

            date = arrayOf(
                "1/1/2024",
                "1/1/2024",
                "1/1/2024"
            )
            amount = arrayOf(
                "LKR 24,000",
                "LKR 50,000",
                "LKR 10,000"
            )

            recyclerView = findViewById(earning_rv)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)

            dataList = arrayListOf<DataClassEarnings>()
            getData()
        }
        private fun getData() {
            for (i in date.indices) {
                val dataClass = DataClassEarnings(date[i], amount[i])
                dataList.add(dataClass)
            }
            recyclerView.adapter = EarningsAdapter(dataList)
        }
    }
