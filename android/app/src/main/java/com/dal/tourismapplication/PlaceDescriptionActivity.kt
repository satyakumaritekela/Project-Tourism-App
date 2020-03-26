package com.dal.tourismapplication

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_place_description.*


class PlaceDescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_description)

        var place_name = txt_locationName;
        var place_image = image;
        var book_button = btn_bookTicket;

        var placeName = intent.getStringExtra("place name")
        var imageURL = intent.getStringExtra("place image")


        place_name.setText(placeName)

        Picasso.get().load(imageURL).into(place_image)

        book_button.setOnClickListener({
            val intent = Intent(this, TicketBooking::class.java)
            startActivity(intent)
        })
    }
}
