package com.wasfan.fixfastbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

data class HelplineAdapter(private val helplineList: List<Helpline>) :
    RecyclerView.Adapter<HelplineAdapter.HelplineViewHolder>() {

    class HelplineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val helplineImageView: ImageView = itemView.findViewById(R.id.helplineImage)
        val helplineNameTV: TextView = itemView.findViewById((R.id.helplineText))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelplineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.helpline_items , parent , false)
        return HelplineViewHolder(view)
    }

    override fun getItemCount(): Int {
        return helplineList.size
    }

    override fun onBindViewHolder(holder: HelplineViewHolder, position: Int) {
        val helpline = helplineList[position]
        holder.helplineImageView.setImageResource(helpline.helplineImage)
        holder.helplineNameTV.text = helpline.helplineName
    }
}
