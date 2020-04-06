package com.dal.tourismapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class TicketBooking : AppCompatActivity() {

    var destinationSpinner: Spinner? = null
    var sourceSpinner: Spinner? = null
    var destinationString = ""
    var addBack = ""
    var destinationList1: ArrayList<String> =
        ArrayList()
    var destinationList2: ArrayList<String> =
        ArrayList()
    var sourceList1: ArrayList<String> = ArrayList()
    var sourceList2: ArrayList<String> = ArrayList()
    private val packageList = ArrayList<JSONObject>()
    private var departDateText: EditText? = null
    var departDateButton: Button? = null
    private var returnDateText: EditText? = null
    var returnDateButton: Button? = null
    var flag = true
    private var ticketPrice: EditText? = null
    private var cardNumber: EditText? = null
    private var cvvNumber: EditText? = null
    var submitDetails: Button? = null
    var departDay = 0
    var departMonth:Int = 0
    var departYear:Int = 0
    var returnDay = 0
    var returnMonth:Int = 0
    var returnYear:Int = 0
    var priceHeading: TextView? = null
    var cardHeading: TextView? = null
    var cvvHeading: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_booking)

        getDetails()
        getPlaceDetails()
    }

    private fun getDetails() {
        destinationSpinner = findViewById<Spinner>(R.id.destination)
        sourceSpinner = findViewById<Spinner>(R.id.source)
        val intent = intent
        if (intent.getStringExtra("dest") != null) {
            destinationString = intent.getStringExtra("dest")
        }
        destinationList2.add(0, destinationString)
        sourceList2.add(0, "Select Source:")
        departDateButton = findViewById<Button>(R.id.depart_button)
        departDateText = findViewById<EditText>(R.id.depart_date)
        returnDateButton = findViewById<Button>(R.id.return_button)
        returnDateText = findViewById<EditText>(R.id.return_date)
        ticketPrice = findViewById<EditText>(R.id.ticket_price_text)
        cardNumber = findViewById<EditText>(R.id.card_number)
        cvvNumber = findViewById<EditText>(R.id.cvv)
        submitDetails = findViewById<Button>(R.id.submit)
        priceHeading = findViewById(R.id.price_heading)
        cardHeading = findViewById(R.id.card_heading)
        cvvHeading = findViewById(R.id.cvv_heading)

        val source_adapter: ArrayAdapter<String>
        source_adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sourceList2)

        val destination_adapter: ArrayAdapter<String>
        destination_adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, destinationList2)

        source_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        destination_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        destinationSpinner!!.setAdapter(destination_adapter)
        sourceSpinner!!.setAdapter(source_adapter)

        sourceSpinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getPrice()
                val item: String = parent!!.getItemAtPosition(position).toString()
                Toast.makeText(parent.getContext(), "Selected: $item", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })

        destinationSpinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getPrice()
                val item: String = parent!!.getItemAtPosition(position).toString()
                Toast.makeText(parent.getContext(), "Selected: " +item, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })

        departDateButton!!.setOnClickListener({
            showDepartDate()
        })

        returnDateButton!!.setOnClickListener({
            showReturnDate()
        })

        submitDetails!!.setOnClickListener(View.OnClickListener {
            if (sourceSpinner!!.getSelectedItem().toString() != destinationSpinner!!.getSelectedItem().toString()
                && cardNumber!!.getText().length == 16 && cvvNumber!!.getText().length == 3
            ) {
                if (returnYear > departYear) {
                    if (returnMonth > departMonth || returnMonth < departMonth || returnMonth == departMonth) {
                        if (returnDay > departDay || returnDay < departDay || returnDay == departDay) {
                            Toast.makeText(
                                applicationContext,
                                "Ticket booked",
                                Toast.LENGTH_LONG
                            ).show()
                            generateTicket()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Ticket cannot be booked",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else if (returnYear == departYear) {
                    if (returnMonth > departMonth) {
                        Toast.makeText(
                            applicationContext,
                            "Ticket booked",
                            Toast.LENGTH_LONG
                        ).show()
                        generateTicket()
                    } else if (returnMonth == departMonth) {
                        if (returnDay == departDay || returnDay > departDay) {
                            Toast.makeText(
                                applicationContext,
                                "Ticket booked",
                                Toast.LENGTH_LONG
                            ).show()
                            generateTicket()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Ticket cannot be booked",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Ticket cannot be booked",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun getPlaceDetails() {
        val requestQueue = Volley.newRequestQueue(this)

        val objectRequest = JsonObjectRequest(
            Request.Method.GET,
            "http://52.90.211.209:81/package/",
            null,
            Response.Listener { response ->
                Log.e("Response", response.toString())
                try {
                    val packages = response["data"] as JSONArray
                    for (i in 0 until packages.length()) {
                        packageList.add(packages[i] as JSONObject)
                    }
                    Log.e("package_list", packageList.toString())
                    for (i in packageList.indices) {
                        try {
                            sourceList1.add(packageList.get(i).get("source").toString())
                            destinationList1.add(packageList.get(i).get("destination").toString())
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    Log.e("Source List", sourceList1.toString())
                    for (i in sourceList1.indices) {
                        if (!sourceList2.contains(sourceList1.get(i))) {
                            sourceList2.add(sourceList1.get(i))
                        }
                    }
                    for (i in destinationList1.indices) {
                        if (!destinationList2.contains(destinationList1.get(i))) {
                            destinationList2.add(destinationList1.get(i))
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Log.e(
                    "Response",
                    error.toString()
                )
            }
        )
        requestQueue.add(objectRequest)
    }

    fun getPrice() {
        for (i in packageList.indices) {
            try {
                if (sourceSpinner!!.getSelectedItem().toString() == packageList.get(i).get("source").toString() && destinationSpinner!!.getSelectedItem().toString() == packageList.get(
                        i
                    ).get("destination").toString()
                ) {
                    ticketPrice!!.setText(packageList.get(i).get("price").toString())
                    Log.e("Price is: ", ticketPrice.toString())
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    fun showDepartDate() {
        flag = true
        val datePickerDialog: DatePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener{ view, year, monthOfYear, dayOfMonth ->},
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    fun showReturnDate() {
        val datePickerDialog1: DatePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener{ view, year, monthOfYear, dayOfMonth -> },
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog1.show()
    }

    fun onDateSet(datePicker: DatePicker?, i: Int, i1: Int, i2: Int) {
        val date: String = (i1 + 1).toString() +  "/" + i2 + "/" + i
        if (flag == true) {
            departDateText!!.setText(date)
            departDay = i2
            departMonth = i1 + 1
            departYear = i
            flag = false
        } else {
            returnDateText!!.setText(date)
            returnDay = i2
            returnMonth = i1 + 1
            returnYear = i
        }
    }

    fun generateTicket() {
        val intent = Intent(this, SampleTicketActivity::class.java)
        intent.putExtra("source1", sourceSpinner!!.getSelectedItem().toString())
        intent.putExtra("destination1", destinationSpinner!!.getSelectedItem().toString())
        intent.putExtra("depart_Date", departDateText!!.getText().toString().trim({ it <= ' ' }))
        intent.putExtra("return_Date", returnDateText!!.getText().toString().trim({ it <= ' ' }))
        intent.putExtra("ticket_price", ticketPrice!!.getText().toString().trim({ it <= ' ' }))
        startActivity(intent)
    }
}
