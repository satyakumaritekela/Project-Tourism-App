package org.dal.mc.optimus.activity.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dal.tourismapplication.PlaceItem

class PlaceItemGridAdapter(var listPlace:List<PlaceItem>):
        RecyclerView.Adapter<PlaceItemGridViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceItemGridViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PlaceItemGridViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return listPlace.size
    }

    override fun onBindViewHolder(holder: PlaceItemGridViewHolder, position: Int) {
        val placeItem = listPlace[position]
        return holder.bind(placeItem)
    }
}