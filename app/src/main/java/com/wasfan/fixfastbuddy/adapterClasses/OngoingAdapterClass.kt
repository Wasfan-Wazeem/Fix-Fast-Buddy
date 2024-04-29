package com.wasfan.fixfastbuddy.adapterClasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy.R
import com.wasfan.fixfastbuddy.dataClasses.OngoingDataClass

class OngoingAdapterClass(
    private val dataList: ArrayList<OngoingDataClass>,
    private val onTrackClickListener: (Int) -> Unit, // Interface for track button click
    private val onCallClickListener: (Int) -> Unit,  // Interface for call button click
    private val onMessageClickListener: (Int) -> Unit // Interface for message button click
) :
    RecyclerView.Adapter<OngoingAdapterClass.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.ongoing_items, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        //holder.rvImage.setImageResource(currentItem.image)
        holder.rvServiceID.text = currentItem.serviceID
        holder.rvServiceType.text = currentItem.serviceType
        holder.rvVehicleNum.text = currentItem.vehicleNum
        holder.rvAddress.text = currentItem.address
        holder.rvDate.text = currentItem.date
        holder.rvStatus.text = currentItem.status
        holder.rvLocationDistance.text = currentItem.locationDistance

        // Set click listeners for buttons
        holder.ongoingTrackBtn.setOnClickListener {
            onTrackClickListener(position) // Call the interface method with position
        }
        holder.ongoingCallBtn.setOnClickListener {
            onCallClickListener(position) // Call the interface method with position
        }
        holder.ongoingMessageBtn.setOnClickListener {
            onMessageClickListener(position) // Call the interface method with position
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //val rvImage: ImageView = itemView.findViewById(R.id.ongoingServiceImage)
        val rvServiceID: TextView = itemView.findViewById((R.id.ongoingServiceID))
        val rvServiceType: TextView = itemView.findViewById((R.id.ongoingServiceType))
        val rvVehicleNum: TextView = itemView.findViewById((R.id.ongoingVehicle))
        val rvAddress: TextView = itemView.findViewById((R.id.ongoingAddress))
        val rvDate: TextView = itemView.findViewById((R.id.ongoingDate))
        val rvStatus: TextView = itemView.findViewById((R.id.ongoingStatus))
        val rvLocationDistance: TextView = itemView.findViewById((R.id.ongoingTimeToLocation))

        val ongoingTrackBtn: ImageButton = itemView.findViewById(R.id.ongoingTrackBtn)
        val ongoingCallBtn: ImageButton = itemView.findViewById(R.id.ongoingCallBtn)
        val ongoingMessageBtn: ImageButton = itemView.findViewById(R.id.ongoingMessageBtn)
    }
}
