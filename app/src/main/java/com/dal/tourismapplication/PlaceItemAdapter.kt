package com.dal.tourismapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.util.*

class PlaceItemAdapter(var locationsList: ArrayList<JSONObject>, var context: Context):
        RecyclerView.Adapter<PlaceItemAdapter.ViewHolder>(), Serializable{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.location_view, parent, false)
        val viewHolder: ViewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return locationsList.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val obj: JSONObject = locationsList.get(position)
        try {
            Picasso.get().load(obj["imagePath"].toString()).into(holder.image)
            holder.txt_locationName.setText(obj["name"].toString())
            holder.txt_cityName.setText(", " + obj["location"].toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        holder.parent_layout.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, PlaceDescriptionActivity::class.java)
            intent.putExtra("name", holder.txt_locationName.getText().toString())
            try {
                intent.putExtra("dest", obj["location"].toString())
                intent.putExtra("imagePath", obj["imagePath"].toString())
                intent.putExtra("type", obj["type"].toString())
                intent.putExtra("keyFeatures", obj["keyFeatures"].toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            context.startActivity(intent)
        })
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_locationName: TextView
        var txt_cityName: TextView
        var parent_layout: RelativeLayout
        var image: ImageView

        init {
            txt_locationName = itemView.findViewById(R.id.txt_locationName)
            txt_cityName = itemView.findViewById(R.id.txt_city)
            image = itemView.findViewById(R.id.image)
            parent_layout = itemView.findViewById(R.id.parent_layout)
        }
    }
}