package com.dal.tourismapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import org.json.JSONException
import org.json.JSONObject
import java.util.*

open class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    /** locations list to render on to the screen **/
    private val locationsList = ArrayList<JSONObject>()
    private val filteredList = ArrayList<JSONObject>()
    private var toolbar: Toolbar? = null
    protected var mNavDrawer: DrawerLayout? = null
    private var navigationView: NavigationView? =null
    private var frameLayout: FrameLayout? = null
    private val mainMenu: Menu? = null
    var recyclerView: RecyclerView? = null

    var adapter: PlaceItemAdapter? = null
    var search: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //frameLayout = findViewById(R.id.activity_frame);
        setContentView(R.layout.nav_drawer_layout)
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

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //supportActionBar(toolbar).hide()

        mNavDrawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        var toggle = ActionBarDrawerToggle(
            this, mNavDrawer, toolbar
            , R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );


        mNavDrawer!!.addDrawerListener(toggle)
        toggle.syncState()
        navigationView!!.setNavigationItemSelectedListener(this)


    }
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {

            R.id.home -> {
                Toast.makeText(this, "Clicked " , Toast.LENGTH_SHORT).show()
            }
            R.id.logout -> {
                val intent= Intent(this@MainActivity ,SignupActivity::class.java)
                startActivity(intent)
            }
            R.id.tickets -> {
                val intent= Intent(this@MainActivity ,SampleTicketActivity::class.java)
                startActivity(intent)
            }

        }
        mNavDrawer!!.closeDrawer(GravityCompat.START)
        return true

    }
    override fun onBackPressed() {
        if (mNavDrawer!!.isDrawerOpen(GravityCompat.START)) {
            mNavDrawer!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun recyclerView(list: ArrayList<JSONObject>) {
        recyclerView = findViewById(R.id.recyclerView)
        adapter = PlaceItemAdapter(list, this)
        recyclerView!!.setAdapter(adapter)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
    }

}

