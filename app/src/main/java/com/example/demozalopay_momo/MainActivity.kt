package com.example.demozalopay_momo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnConfirm: Button
    private lateinit var edtSoluong: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Ánh xạ view
        btnConfirm = findViewById(R.id.buttonConfirm)
        edtSoluong = findViewById(R.id.editTextSoluong)


        btnConfirm.setOnClickListener {
            val soluongText = edtSoluong.text?.toString()
            if (soluongText.isNullOrEmpty()) {
                Toast.makeText(this, "Nhập số lượng muốn mua", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val soLuong = soluongText.toDoubleOrNull()
            if (soLuong == null) {
                Toast.makeText(this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val total = soLuong * 1000.0
            val intent = Intent(this, OrderPaymentActivity::class.java).apply {
                putExtra("soluong", soluongText)
                putExtra("total", total)
            }
            startActivity(intent)
        }
    }
}