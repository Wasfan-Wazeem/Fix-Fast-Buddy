package com.wasfan.fixfastbuddy.adapterClasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy.R

class CancellationReasonAdapter(
    private val reasons: List<String>,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<CancellationReasonAdapter.ViewHolder>() {

    var lastCheckedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cancellation_reason_item_layout, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reasons[position], lastCheckedPosition == position)
    }

    override fun getItemCount() = reasons.size

    inner class ViewHolder(view: View, onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        private val textViewReason: TextView = view.findViewById(R.id.ReasonTV)
        private val radioButton: RadioButton = view.findViewById(R.id.radioButton)

        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    lastCheckedPosition = adapterPosition
                    onItemClicked(adapterPosition)
                    notifyDataSetChanged()
                }
            }
            radioButton.setOnClickListener {
                view.performClick() // Delegate the click event to the view's click listener
            }
        }

        fun bind(reason: String, isSelected: Boolean) {
            textViewReason.text = reason
            radioButton.isChecked = isSelected
        }
    }
}

