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
    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        this.onItemClickListener = listener
    }

    inner class ImageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imageView : ImageView = itemView.findViewById(R.id.vehicleCardImage)
        val vehicleCardText : TextView = itemView.findViewById(R.id.vehicleCardText)

        init {
            itemView.setOnClickListener {
                // Call the onItemClick lambda function if set
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_image_container , parent , false)
        return ImageViewHolder(view)
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