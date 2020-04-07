package com.dal.tourismapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class ViewTicketsActivity: OptionsMenuActivity() {

    private var toolbar: Toolbar? = null
    var recyclerView: RecyclerView? = null
    var adapter: PlaceItemAdapter? = null
    private val ticketsList = ArrayList<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_tickets)
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar()!!.setTitle("Tourism Application");

        val requestQueue = Volley.newRequestQueue(this)

        var objRequest = JsonObjectRequest(
            Request.Method.GET, "http://52.90.211.209:81/location/", null,
            Response.Listener<JSONObject> { response ->

                Log.e("Response", response.toString())
                try {
                    val locations = response
                        .getJSONArray("data")

                    for (i in 0 until locations.length()) {
                        ticketsList.add(locations[i] as JSONObject)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            },
            Response.ErrorListener { error ->
                Log.e("Response", error.toString())
            })

        requestQueue.add(objRequest)
        recyclerView(ticketsList)
    }

    private fun recyclerView(list: ArrayList<JSONObject>) {
        recyclerView = findViewById(R.id.recyclerView)
        adapter = PlaceItemAdapter(list, this)
        recyclerView!!.setAdapter(adapter)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
    }
}