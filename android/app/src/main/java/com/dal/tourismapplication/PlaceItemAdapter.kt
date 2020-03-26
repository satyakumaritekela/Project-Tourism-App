package org.dal.mc.optimus.activity.ui.home

import PlaceItemViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dal.tourismapplication.PlaceItem

class PlaceItemAdapter(var listPlace:List<PlaceItem>):
        RecyclerView.Adapter<PlaceItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PlaceItemViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return listPlace.size
    }

    override fun onBindViewHolder(holder: PlaceItemViewHolder, position: Int) {
        val placeItem = listPlace[position]
        return holder.bind(placeItem)
    }
}