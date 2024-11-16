package com.example.demozalopay_momo

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class PaymentNotificationActivity : AppCompatActivity() {

    private lateinit var txtNotification: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_notification)
        txtNotification = findViewById(R.id.textViewNotify)

        val intent = intent
        txtNotification.text = intent.getStringExtra("result")

    }
}