package com.dal.tourismapplication

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

class SampleTicketActivity: MainActivity() {
    private var ticketId: TextView? = null
    private var source2: TextView? = null
    private var destination2: TextView? = null
    private var departureDate2: TextView? = null
    private var returnDate2: TextView? = null
    private var price: TextView? = null
    private var bCode: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = this
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = inflater.inflate(R.layout.activity_sample_ticket, null, false)
        mNavDrawer?.addView(contentView, 0)

      //  setContentView(R.layout.activity_sample_ticket)

        getDetails()

        val sourceDetails = intent.extras!!.getString("source1")

        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(
                sourceDetails,
                BarcodeFormat.CODE_128,
                400,
                200
            )
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)

            bCode!!.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        printDetails()
    }

    private fun getDetails() {
        ticketId = findViewById<TextView>(R.id.ticket_id)
        source2 = findViewById(R.id.src)
        destination2 = findViewById(R.id.dst)
        departureDate2 = findViewById<TextView>(R.id.departure_date)
        returnDate2 = findViewById<TextView>(R.id.return_date)
        price = findViewById(R.id.price)
        bCode = findViewById(R.id.bar_code)
    }

    private fun printDetails() {
        ticketId!!.setText("Ticket1123")
        val source = intent.extras!!.getString("source1")
        val destination = intent.extras!!.getString("destination1")
        source2!!.setText("$source to $destination")
        val departDate = intent.extras!!.getString("depart_Date")
        val returnDate = intent.extras!!.getString("return_Date")
        departureDate2!!.setText("$departDate to $returnDate")
        val ticketprice = intent.extras!!.getString("ticket_price")
        price!!.setText("Ticket Price: $$ticketprice")
    }

}