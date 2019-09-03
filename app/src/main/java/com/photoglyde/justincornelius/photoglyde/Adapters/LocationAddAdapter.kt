package com.photoglyde.justincornelius.photoglyde.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.photoglyde.justincornelius.photoglyde.R
import kotlinx.android.synthetic.main.location_container.view.*

class LocationAddAdapter(val userList: ArrayList<String?>, val clickListener: (ArrayList<String?>, Int) -> Unit) : RecyclerView.Adapter<LocationAddAdapter.ViewHolder>() {
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.location_container, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position])
        (holder).bind(userList, clickListener)

        val margins = holder.itemView.card_location_holder.layoutParams as LinearLayout.LayoutParams

        when(position){

            0 -> {
                margins.setMargins(0,0,20,0)
            }

            else ->{
                margins.setMargins(20,0,20,0)
            }
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(location: String?) {
            val textViewName = itemView.findViewById(R.id.location_text) as TextView
            println("this is from the adapter $location} and ")



            textViewName.text = location

        }

        fun bind(part: ArrayList<String?>, clickListener: (ArrayList<String?>, Int) -> Unit) {


            var position: Int = adapterPosition
            itemView.setOnClickListener { clickListener(part, position) }


        }
    }
}