package com.dal.tourismapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    /** locations list to render on to the screen **/
    private val locationsList = ArrayList<JSONObject>()
    private val filteredList = ArrayList<JSONObject>()

    private val mainMenu: Menu? = null
    var recyclerView: RecyclerView? = null

    var adapter: PlaceItemAdapter? = null
    var search: EditText? = null    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (getSupportActionBar() != null)
            supportActionBar!!.hide()

        search = findViewById(R.id.searchbar)

        search!!.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filteredList.clear()

                locationsList.forEachIndexed { index, jsonObject ->
                    val obj: JSONObject = locationsList.get(index)
                    try {
                        if (obj["name"].toString().toLowerCase().contains(
                                search!!.text.toString().trim { it <= ' ' }.toLowerCase()
                            ) || obj["location"].toString().toLowerCase().contains(
                                search!!.text.toString().trim { it <= ' ' }.toLowerCase()
                            )
                        ) {
                            filteredList.add(obj)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                recyclerView(filteredList)
            }
        })

        val requestQueue = Volley.newRequestQueue(this)

        var objRequest = JsonObjectRequest(Request.Method.GET, "http://52.90.211.209:81/location/", null,
            Response.Listener<JSONObject> {response ->

                Log.e("Response", response.toString())
                try {
                    val locations = response
                        .getJSONArray("data")

                    for (i in 0 until locations.length()) {
                        locationsList.add(locations[i] as JSONObject)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            },
            Response.ErrorListener { error ->
                Log.e("Response", error.toString())
            })

        requestQueue.add(objRequest)
        recyclerView(locationsList)

    }

    private fun recyclerView(list: ArrayList<JSONObject>) {
        recyclerView = findViewById(R.id.recyclerView)
        adapter = PlaceItemAdapter(list, this)
        recyclerView!!.setAdapter(adapter)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
    }

}

