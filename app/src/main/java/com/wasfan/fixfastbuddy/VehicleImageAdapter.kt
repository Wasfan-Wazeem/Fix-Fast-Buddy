package com.wasfan.fixfastbuddy

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VehicleImageAdapter(private val context: Context, private val imageList : ArrayList<Int>, private val vehicleList : ArrayList<String>)
    :RecyclerView.Adapter<VehicleImageAdapter.ImageViewHolder>()
{
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener

    }

    inner class ImageViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val imageView : ImageView = itemView.findViewById(R.id.vehicleCardImage)
        val vehicleCardText : TextView = itemView.findViewById(R.id.vehicleCardText)

        init {
            itemView.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_image_container , parent , false)
        return ImageViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageResource(imageList[position])
        holder.vehicleCardText.text = vehicleList[position]

    }

}


/*holder.itemView.setOnClickListener {
            if(position == imageList.size - 1) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("OTP", verificationId)
                intent.putExtra("resendToken", token)
                intent.putExtra("phoneNumber", number)
                context.startActivity(intent)
            }else{
                val intent = Intent(context, VehicleInfo::class.java)
                context.startActivity(intent)
            }

        }*/