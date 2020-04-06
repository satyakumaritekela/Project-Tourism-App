package com.dal.tourismapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import java.net.MalformedURLException
import java.net.URL
import java.util.regex.Pattern

class LoginActivity: AppCompatActivity() {

    var emailEditText: TextInputEditText? = null
    var passwordEditText:TextInputEditText? = null
    var emailInputLayout:TextInputLayout? = null
    var passwordInputLayout:TextInputLayout? = null
    var btnLogin: Button? = null
    var btnCreateAccount: Button? = null
    var email: String? = null
    var password: String? = null
    var backPressSingleTime = false
    var destination: String? = null

    val PATTERNPASSWORD = Pattern.compile(
        "^" +
                "(?=.*[0-9])" +
                "(?=.*[a-z])" +
                "(?=.*[A-Z])" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{8,24}" +
                "$"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val intent = intent
        if (intent.getStringExtra("dest") != null) {
            destination = intent.getStringExtra("dest")
        }

        getDetails()

        btnLogin!!.setOnClickListener({
            if (validateEMAIL()!! && validatePassword()!!) {
                val obj: JSONObject = JSONObject()
                try {
                    obj.put("otp", "")
                    obj.put(
                        "password",
                        passwordEditText!!.text.toString().trim { it <= ' ' }
                    )
                    obj.put("email", emailEditText!!.text.toString().trim { it <= ' ' })
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                loginAPI(obj)
            }
        })

        btnCreateAccount!!.setOnClickListener(View.OnClickListener {
            val intentToSignup =
                Intent(applicationContext, SignupActivity::class.java)
            intentToSignup.putExtra("dest", destination)
            startActivity(intentToSignup)
        })

    }

    private fun loginAPI(obj: JSONObject) {
        val requestQueue = Volley.newRequestQueue(this)

        try {
            val url = URL("http://100.26.195.40:82/auth/login")
            val objectRequest = JsonObjectRequest(
                Request.Method.POST,
                url.toString(),
                obj,
                Response.Listener { response ->
                    try {
                        val resp = response["status"].toString()
                        if (resp == "success") {
                            val intent =
                                Intent(this@LoginActivity, AuthenticationActivity::class.java)
                            intent.putExtra(
                                "email",
                                emailEditText!!.text.toString().trim { it <= ' ' }
                            )
                            intent.putExtra("dest", destination)
                            startActivity(intent)
                        } else {
                            Log.e("Response", "Invalid Credentials")
                            val toast = Toast.makeText(
                                applicationContext,
                                "Invalid Credentials",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
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

    private fun getDetails() {
        emailEditText = findViewById(R.id.et_email)
        passwordEditText = findViewById(R.id.et_password)
        btnCreateAccount = findViewById(R.id.btn_createAccount)
        btnLogin = findViewById(R.id.btn_login)
        emailInputLayout = findViewById(R.id.inputLayout_email)
        passwordInputLayout = findViewById(R.id.inputLayout_password)
    }

    override fun onBackPressed() {
        if (backPressSingleTime) {
            super.onBackPressed()
            return
        }
        backPressSingleTime = true
        Toast.makeText(applicationContext, "Back", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ backPressSingleTime = false }, 2000)
    }

    fun validateEMAIL(): Boolean? {
        email = emailEditText!!.text.toString().trim { it <= ' ' }
        return if (email!!.isEmpty()) {
            emailInputLayout!!.error = "Field should not be empty"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout!!.error = "Please enter a valid email"
            false
        } else {
            emailInputLayout!!.error = null
            true
        }
    }

    fun validatePassword(): Boolean? {
        password = passwordEditText!!.text.toString().trim { it <= ' ' }
        return if (password!!.isEmpty()) {
            passwordInputLayout!!.error = "Field should not be empty"
            false
        } else if (!PATTERNPASSWORD.matcher(password).matches()) {
            passwordInputLayout!!.error = "Password should be between 8 to 24 character\n" +
                    "at least 1 special character [@#$%^&+=]\n" +
                    "at least 1 digit\n" +
                    "at least 1 capital letter\n" +
                    "at least 1 small letter\n"
            passwordEditText!!.setText("")
            false
        } else {
            passwordInputLayout!!.error = null
            true
        }
    }

}