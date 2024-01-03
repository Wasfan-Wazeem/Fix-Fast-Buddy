package com.wasfan.fixfastbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ServicesAdapterClass(private val servicesList : List<ServicesDataClass>) :
    RecyclerView.Adapter<ServicesAdapterClass.ServicesViewHolder>(){
    class ServicesViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val servicesImageView: ImageView = itemView.findViewById(R.id.servicesImage)
        val servicesNameTV: TextView = itemView.findViewById((R.id.servicesText))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.services_items, parent, false)
        return ServicesViewHolder(view)
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