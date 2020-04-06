package com.dal.tourismapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class PlaceDescriptionActivity : AppCompatActivity() {

    var locationName: String? = null
    var city: String? = null
    var type: String? = null
    var keyFeatures: String? = null
    var imagePath: String? = null
    var locationsList = ArrayList<JSONObject>()
    var locationObj: JSONObject? = null
    var btnBook: Button? = null
    var image: ImageView? = null
    var location: TextView? = null
    var keyFeat:TextView? = null
    var locType:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_description)

        image = findViewById(R.id.image)
        location = findViewById(R.id.location_tv)
        keyFeat = findViewById(R.id.keyfeat_content)
        locType = findViewById(R.id.type_content)

        val intent = intent

        btnBook = findViewById<Button>(R.id.btn_book)
        if (intent.getStringExtra("name") != null) {
            locationName = intent.getStringExtra("name")
        }
        if (intent.getStringExtra("dest") != null) {
            city = intent.getStringExtra("dest")
        }
        if (intent.getStringExtra("imagePath") != null) {
            imagePath = intent.getStringExtra("imagePath")
        }
        if (intent.getStringExtra("type") != null) {
            type = intent.getStringExtra("type")
        }
        if (intent.getStringExtra("keyFeatures") != null) {
            keyFeatures = intent.getStringExtra("keyFeatures")
        }

        Picasso.get().load(imagePath).into(image)
        location!!.setText(locationName + ", " + city)
        locType!!.setText(type)
        keyFeat!!.setText(keyFeatures)

        locationsList.indices.forEachIndexed { index, jsonObject ->
            val obj: JSONObject = locationsList.get(index)
            try {
                if (obj["name"].toString().toLowerCase().contains(locationName!!.toLowerCase())) {
                    locationObj = obj
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        btnBook!!.setOnClickListener({
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("dest", city)
            startActivity(intent)
        })

    }
}
