package com.wasfan.fixfastbuddy.adapterClasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy.CompletedDataClass
import com.wasfan.fixfastbuddy.R

class CompletedAdapterClass(private val dataList: ArrayList<CompletedDataClass>,
                            private val onRatingClickListener: (Int) -> Unit, // Interface for track button click
                            private val onViewDetailsClickListener: (Int) -> Unit, // Interface for track button click
) :
    RecyclerView.Adapter<CompletedAdapterClass.ViewHolderClass>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.completed_items, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.image!!)
        holder.rvServiceID.text = currentItem.serviceID
        holder.rvServiceType.text = currentItem.serviceType
        holder.rvVehicleNum.text = currentItem.vehicleNum
        holder.rvAddress.text = currentItem.address
        holder.rvDate.text = currentItem.date
        holder.rvTime.text = currentItem.time
        holder.rvCompletedAmount.text = currentItem.completedAmount

        // Set click listeners for buttons
        holder.completedRateNowBtn.setOnClickListener {
            onRatingClickListener(position) // Call the interface method with position
        }
        holder.completedDetailsTV.setOnClickListener {
            onViewDetailsClickListener(position) // Call the interface method with position
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {

        val rvImage: ImageView = itemView.findViewById(R.id.ongoingServiceImage)
        val rvServiceID: TextView = itemView.findViewById((R.id.ongoingServiceID))
        val rvServiceType: TextView = itemView.findViewById((R.id.ongoingServiceType))
        val rvVehicleNum: TextView = itemView.findViewById((R.id.ongoingVehicle))
        val rvAddress: TextView = itemView.findViewById((R.id.ongoingAddress))
        val rvDate: TextView = itemView.findViewById((R.id.ongoingDate))
        val rvTime: TextView = itemView.findViewById((R.id.ongoingTime))
        val rvCompletedAmount: TextView = itemView.findViewById((R.id.completedAmount))

        val completedRateNowBtn: Button = itemView.findViewById(R.id.completedRateNowBtn)
        val completedDetailsTV: TextView = itemView.findViewById(R.id.completedDetails)
    }
}