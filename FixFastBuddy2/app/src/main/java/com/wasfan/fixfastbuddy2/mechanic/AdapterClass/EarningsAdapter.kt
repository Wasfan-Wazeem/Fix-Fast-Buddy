package com.wasfan.fixfastbuddy2.mechanic.AdapterClass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.mechanic.DataClass.DataClassEarnings

class EarningsAdapter(private val dataList: ArrayList<DataClassEarnings>): RecyclerView.Adapter<EarningsAdapter.ViewHolderClass1>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass1 {
      val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_earnings, parent, false)
      return ViewHolderClass1(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass1, position: Int) {
        val currentItem = dataList[position]
        holder.rvTitleEarnings.text = currentItem.dataTitle
    }

    class ViewHolderClass1(itemView:View): RecyclerView.ViewHolder(itemView) {
        val rvTitleEarnings: TextView = itemView.findViewById(R.id.seperate_earnings)

    }

}
