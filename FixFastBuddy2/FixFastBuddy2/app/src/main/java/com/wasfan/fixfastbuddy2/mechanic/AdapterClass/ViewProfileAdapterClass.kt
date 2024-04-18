package com.wasfan.fixfastbuddy2.mechanic.AdapterClass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.mechanic.DataClass.ViewProfileDataClass

class ViewProfileAdapterClass(private val dataList: ArrayList<ViewProfileDataClass>): RecyclerView.Adapter<ViewProfileAdapterClass.ViewHolderClass>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int, text : String)

    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_profile_items, parent, false)
        return ViewHolderClass(itemView,mListener)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvTitle.text = currentItem.title
        holder.rvInfo.text = currentItem.info
    }

    class ViewHolderClass(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        val rvTitle:TextView = itemView.findViewById(R.id.titleText)
        val rvInfo:TextView = itemView.findViewById(R.id.infoText)

        init{

            itemView.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val text = rvTitle.text.toString()
                    listener.onItemClick(position, text)
                }
            }

        }
    }

}