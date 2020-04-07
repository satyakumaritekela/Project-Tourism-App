package com.dal.tourismapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.net.MalformedURLException
import java.net.URL

class AuthenticationActivity: OptionsMenuActivity() {
    var btnSubmitCode: Button? = null
    var inputCode: EditText? = null
    var email: String? = null
    var authToken: String? = null
    var dest: String? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        val intent = intent

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar()!!.setTitle("Tourism Application");

        if (intent.getStringExtra("email") != null) {
            email = intent.getStringExtra("email")
        }
        if (intent.getStringExtra("dest") != null) {
            dest = intent.getStringExtra("dest")
        }

        btnSubmitCode = findViewById<Button>(R.id.btn_submitCode)
        inputCode = findViewById<EditText>(R.id.input_code)

        btnSubmitCode!!.setOnClickListener({
            val obj: JSONObject = JSONObject()
            try {
                obj.put("otp", inputCode!!.getText().toString().trim({ it <= ' ' }))
                obj.put("password", "")
                obj.put("email", email)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            authenticateAPI(obj)
        })

    }

    private fun authenticateAPI(obj: JSONObject) {
        val requestQueue = Volley.newRequestQueue(this)

        try {
            val url = URL("http://100.26.195.40:82/auth/loginSecond")
            val objectRequest = JsonObjectRequest(
                Request.Method.POST,
                url.toString(),
                obj,
                Response.Listener { response ->
                    try {
                        Log.e("Response", response.toString())
                        val resp = response["status"].toString()
                        authToken = response["Authorization"].toString()
                        if (resp == "success") {
                            val intent = Intent(
                                this@AuthenticationActivity,
                                TicketBooking::class.java
                            )

                            intent.putExtra("email", email)
                            intent.putExtra("dest", dest)
                            startActivity(intent)
                        } else {
                            Log.e("Response", "Invalid Code")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    Log.e("Response", response.toString())
                },
                Response.ErrorListener { error -> Log.e("Response", error.toString()) }
            )
            objectRequest.retryPolicy = DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            requestQueue.add(objectRequest)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }

}