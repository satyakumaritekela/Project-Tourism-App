package com.dal.tourismapplication

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject
import java.net.MalformedURLException
import java.net.URL
import java.util.regex.Pattern

class SignupActivity: AppCompatActivity() {
    var etEnterEmail: EditText? = null
    var etEnterPassword:EditText? = null
    var etReenterPassword:EditText? = null
    var etFirstName:EditText? = null
    var etLastName:EditText? = null
    var etPhoneNumber:EditText? = null
    var btnSignup: Button? = null
    var enteredEmail: String? = null
    var enteredPassword:kotlin.String? = null
    var reenteredPassword:kotlin.String? = null
    var firstName:kotlin.String? = null
    var gender: String? = null
    var inputLayoutEnterEmail: TextInputLayout? = null
    var inputLayoutEnterPassword:TextInputLayout? = null
    var inputLayoutReenterPassword:TextInputLayout? = null
    var inputLayoutFirstName: TextInputLayout? = null
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
    var passwordError = "Password should be between 8 to 24 character\n" +
            "at least 1 special character [@#$%^&+=]\n" +
            "at least 1 digit\n" +
            "at least 1 capital letter\n" +
            "at least 1 small letter\n"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_signup)

        val intent = intent
        if (intent.getStringExtra("dest") != null) {
            destination = intent.getStringExtra("dest")
        }

        getDetails()

        btnSignup!!.setOnClickListener({
            if (isvalidEmail()!! && isvalidPassword()!! && isvalidConfirmPassword()!! && isvalidUsername()!!) {
                val obj: JSONObject = JSONObject()
                try {
                    obj.put("firstName", etFirstName!!.text.toString().trim { it <= ' ' })
                    obj.put("gender", gender!!.trim { it <= ' ' })
                    obj.put("lastName", etLastName!!.text.toString().trim { it <= ' ' })
                    obj.put(
                        "password",
                        etEnterPassword!!.text.toString().trim { it <= ' ' }
                    )
                    obj.put("email", etEnterEmail!!.text.toString().trim { it <= ' ' })
                    obj.put("phoneNo", etPhoneNumber!!.text.toString().trim { it <= ' ' })
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                signUpAPI(obj)
            }
        })
    }

    private fun signUpAPI(obj: JSONObject) {
        val requestQueue = Volley.newRequestQueue(this)

        try {
            val url = URL("http://100.26.195.40:82/user/")
            val objectRequest = JsonObjectRequest(
                Request.Method.POST,
                url.toString(),
                obj,
                Response.Listener { response ->
                    var resp: String? = null
                    try {
                        resp = response["status"].toString()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (resp == "success") {
                        Log.e("Response from server:", response.toString())
                        val intent =
                            Intent(this@SignupActivity, LoginActivity::class.java)
                        intent.putExtra("dest", destination)
                        startActivity(intent)
                    } else {
                        Log.e("Response", "User already registered")
                        val toast = Toast.makeText(
                            applicationContext,
                            "User already registered",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response from server:", error.toString())
                    val toast = Toast.makeText(
                        applicationContext,
                        "Registration Error",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                })
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
        etEnterEmail = findViewById(R.id.et_enterEmail)
        etEnterPassword = findViewById(R.id.et_enterPassword)
        etReenterPassword = findViewById(R.id.et_reenterPassword)
        etFirstName = findViewById(R.id.et_firstName)
        etLastName = findViewById(R.id.et_lastName)
        btnSignup = findViewById(R.id.btn_signup)
        inputLayoutEnterEmail = findViewById(R.id.inputLayout_enterEmail)
        inputLayoutEnterPassword = findViewById(R.id.inputLayout_enterPassword)
        inputLayoutReenterPassword = findViewById(R.id.inputLayout_reenterPassword)
        inputLayoutFirstName = findViewById(R.id.inputLayout_firstName)
    }

    fun isvalidUsername(): Boolean? {
        firstName = etFirstName!!.text.toString().trim { it <= ' ' }
        return if (firstName!!.isEmpty()) {
            inputLayoutFirstName!!.error = "Field should not be empty"
            false
        } else {
            inputLayoutFirstName!!.error = null
            true
        }
    }

    fun isvalidEmail(): Boolean? {
        enteredEmail = etEnterEmail!!.text.toString().trim { it <= ' ' }
        return if (enteredEmail!!.isEmpty()) {
            inputLayoutEnterEmail!!.error = "Field should not be empty"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()) {
            inputLayoutEnterEmail!!.error = "Please enter a valid EMAIL address"
            false
        } else {
            inputLayoutEnterEmail!!.error = null
            true
        }
    }

    private fun isvalidPassword(): Boolean? {
        enteredPassword = etEnterPassword!!.text.toString().trim { it <= ' ' }
        return if (enteredPassword!!.isEmpty()) {
            inputLayoutEnterPassword!!.error = "Field should not be empty"
            false
        } else if (!PATTERNPASSWORD.matcher(enteredPassword).matches()) {
            inputLayoutEnterPassword!!.error = passwordError
            etEnterPassword!!.setText("")
            false
        } else {
            inputLayoutEnterPassword!!.error = null
            true
        }
    }

    private fun isvalidConfirmPassword(): Boolean? {
        reenteredPassword = etReenterPassword!!.text.toString().trim { it <= ' ' }
        return if (reenteredPassword!!.isEmpty()) {
            inputLayoutReenterPassword!!.error = "Field should not be empty"
            false
        } else if (enteredPassword != reenteredPassword) {
            inputLayoutReenterPassword!!.error = "Doesn't match the given Password"
            etReenterPassword!!.setText("")
            false
        } else {
            inputLayoutReenterPassword!!.error = null
            true
        }
    }
}