package com.wasfan.fixfastbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy.dataClasses.OngoingDataClass

class OngoingAdapterClass(private val dataList: ArrayList<OngoingDataClass>) :
    RecyclerView.Adapter<OngoingAdapterClass.ViewHolderClass>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ongoing_items, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.image)
        holder.rvServiceID.text = currentItem.serviceID
        holder.rvServiceType.text = currentItem.serviceType
        holder.rvVehicleNum.text = currentItem.vehicleNum
        holder.rvAddress.text = currentItem.address
        holder.rvDate.text = currentItem.date
        holder.rvStatus.text = currentItem.status
        holder.rvLocationDistance.text = currentItem.locationDistance
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {

        val rvImage:ImageView = itemView.findViewById(R.id.ongoingServiceImage)
        val rvServiceID:TextView = itemView.findViewById((R.id.ongoingServiceID))
        val rvServiceType:TextView = itemView.findViewById((R.id.ongoingServiceType))
        val rvVehicleNum:TextView = itemView.findViewById((R.id.ongoingVehicle))
        val rvAddress:TextView = itemView.findViewById((R.id.ongoingAddress))
        val rvDate:TextView = itemView.findViewById((R.id.ongoingDate))
        val rvStatus:TextView = itemView.findViewById((R.id.ongoingStatus))
        val rvLocationDistance:TextView = itemView.findViewById((R.id.ongoingTimeToLocation))

    }
}
