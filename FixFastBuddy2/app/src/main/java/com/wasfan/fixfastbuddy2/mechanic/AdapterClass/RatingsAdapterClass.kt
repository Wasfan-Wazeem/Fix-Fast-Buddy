package com.wasfan.fixfastbuddy2.mechanic.AdapterClass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.mechanic.DataClass.RatingsDataClass

class RatingsAdapterClass(private val dataList: ArrayList<RatingsDataClass>): RecyclerView.Adapter<RatingsAdapterClass.ViewHolderClass>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ratings_item, parent, false)
       return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvRating.rating = currentItem.Rating
        holder.rvName.text = currentItem.Name
        holder.rvDate.text = currentItem.Date
        holder.rvReview.text = currentItem.Review
    }

    class ViewHolderClass(itemView: View):RecyclerView.ViewHolder(itemView) {
        val rvRating:RatingBar = itemView.findViewById(R.id.ratingBar3)
        val rvName:TextView = itemView.findViewById(R.id.rating_name)
        val rvDate:TextView = itemView.findViewById(R.id.rating_date)
        val rvReview:TextView = itemView.findViewById(R.id.rating_review)
    }
}