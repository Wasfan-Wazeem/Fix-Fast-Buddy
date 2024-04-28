package com.wasfan.fixfastbuddy.adapterClasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy.R
import com.wasfan.fixfastbuddy.dataClasses.ServicesDataClass

class ServicesAdapterClass(private val servicesList : List<ServicesDataClass>) :
    RecyclerView.Adapter<ServicesAdapterClass.ServicesViewHolder>(){

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener

    }

    class ServicesViewHolder(itemView:View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val servicesImageView: ImageView = itemView.findViewById(R.id.servicesImage)
        val servicesNameTV: TextView = itemView.findViewById((R.id.servicesText))

        init{

            itemView.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.services_items, parent, false)
        return ServicesViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return servicesList.size
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        val services = servicesList[position]
        holder.servicesImageView.setImageResource(services.servicesImage)
        holder.servicesNameTV.text = services.servicesName
    }
}