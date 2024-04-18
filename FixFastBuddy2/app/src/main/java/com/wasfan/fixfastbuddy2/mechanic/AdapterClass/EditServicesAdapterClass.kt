package com.wasfan.fixfastbuddy2.mechanic.AdapterClass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.mechanic.DataClass.EditServicesDataClass

class EditServicesAdapterClass(private val dataList: ArrayList<EditServicesDataClass>): RecyclerView.Adapter<EditServicesAdapterClass.ViewHolderClass>()  {

    var onItemClick: ((EditServicesDataClass) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_edit_services, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvTitle.text = currentItem.Title.toString()

        holder.checkbox.isChecked = currentItem.isChecked
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            currentItem.isChecked = isChecked
            onItemClick?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvTitle:TextView = itemView.findViewById(R.id.titleES)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkBoxES)
    }
}

