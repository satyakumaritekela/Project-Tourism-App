package org.dal.mc.optimus.activity.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dal.tourismapplication.PlaceDescriptionActivity
import com.dal.tourismapplication.PlaceItem
import com.dal.tourismapplication.R


class PlaceItemGridViewHolder (inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.place_grid_item, parent, false)){

    private var placeImg: ImageView
    private var placeName: TextView
    private val context: Context

    init {
        placeImg = itemView.findViewById(R.id.img_place_item_grid)
        placeName = itemView.findViewById(R.id.txt_place_item_grid)
        context = parent.context
    }

    fun bind(placeItem: PlaceItem){
        var img_URL: String = placeItem.imgUrl
        Glide.with(itemView).load(placeItem.imgUrl).apply( RequestOptions()
            .transform( MultiTransformation( CenterCrop(),RoundedCorners(16))))
            .into(placeImg)
        placeName.text = placeItem.placeName

        placeImg.setOnClickListener({
            val intent = Intent(context, PlaceDescriptionActivity::class.java)
            intent.putExtra("place name", placeName.text.toString())
            intent.putExtra("place image", img_URL)
            context.startActivity(intent)
        });
    }

}