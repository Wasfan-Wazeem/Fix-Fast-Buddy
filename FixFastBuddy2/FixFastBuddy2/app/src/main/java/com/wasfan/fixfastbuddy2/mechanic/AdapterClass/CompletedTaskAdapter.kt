package com.wasfan.fixfastbuddy2.mechanic.AdapterClass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.wasfan.fixfastbuddy2.R
import com.wasfan.fixfastbuddy2.mechanic.CompletedTask
import com.wasfan.fixfastbuddy2.mechanic.DetailDialogFragment

class CompletedTaskAdapter(private val tasks: List<CompletedTask>,private val fragmentManager: FragmentManager):RecyclerView.Adapter<CompletedTaskAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompletedTaskAdapter.MyViewHolder {
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


        fun bind(completedTask : CompletedTask){
            name.text = completedTask.name
            task.text = completedTask.task
            date.text = completedTask.date

            // Set click listener for the detail button
            detailButton.setOnClickListener {
                val detailDialogFragment = DetailDialogFragment()
                detailDialogFragment.show(fragmentManager, "DetailDialogFragment")

                // Create a bundle to pass data to the dialog fragment
                val bundle = Bundle()
                bundle.putString("name", completedTask.name)
                bundle.putString("date", completedTask.date)
                bundle.putString("task", completedTask.task)

                // Set the arguments for the dialog fragment
                detailDialogFragment.arguments = bundle


            }
        }


    }
}