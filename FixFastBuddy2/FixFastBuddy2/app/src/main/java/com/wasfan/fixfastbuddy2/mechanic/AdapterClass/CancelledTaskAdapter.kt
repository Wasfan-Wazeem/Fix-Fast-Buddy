package com.wasfan.fixfastbuddy2.mechanic.AdapterClass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.mechanic.DataClass.CancelledTask
import com.wasfan.fixfastbuddy2.mechanic.DetailDialogFragment

class CancelledTaskAdapter (private val tasks: List<CancelledTask>, private val fragmentManager: FragmentManager):
    RecyclerView.Adapter<CancelledTaskAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CancelledTaskAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.complete_task_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)

    }

    override fun getItemCount(): Int {
        return tasks.size
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tv_name)
        private val task: TextView = itemView.findViewById(R.id.tv_task)
        private val date: TextView = itemView.findViewById(R.id.tv_date)
        private val detailButton: TextView = itemView.findViewById(R.id.btn_detail)
        private val reason: TextView = itemView.findViewById(R.id.tv_reason)

        init {
            // Set the initial visibility of the reason TextView
            reason.visibility = View.VISIBLE
        }


        fun bind(cancelledTask: CancelledTask) {
            name.text = cancelledTask.name
            task.text = cancelledTask.task
            date.text = cancelledTask.date
            reason.text = cancelledTask.reason


            // Set click listener for the detail button
            detailButton.setOnClickListener {
                val detailDialogFragment = DetailDialogFragment()
                detailDialogFragment.show(fragmentManager, "DetailDialogFragment")
                val bundle = Bundle()
                bundle.putString("name", cancelledTask.name)
                bundle.putString("date", cancelledTask.date)
                bundle.putString("task", cancelledTask.task)

                // Set the arguments for the dialog fragment
                detailDialogFragment.arguments = bundle

            }
        }


    }
}