package com.example.demozalopay_momo

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.demozalopay_momo.Api.CreateOrder
import org.json.JSONObject
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener


class OrderPaymentActivity : AppCompatActivity() {


    private lateinit var txtSoluong: TextView
    private lateinit var txtTongTien: TextView
    private lateinit var btnThanhToan: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_payment)

        // Ánh xạ view
        txtSoluong = findViewById(R.id.textViewSoluong)
        txtTongTien = findViewById(R.id.textViewTongTien)
        btnThanhToan = findViewById(R.id.buttonThanhToan)

        // Cấu hình StrictMode
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // Khởi tạo ZaloPay SDK
        ZaloPaySDK.init(2553, Environment.SANDBOX)

        val intent = intent
        txtSoluong.text = intent.getStringExtra("soluong")
        val total = intent.getDoubleExtra("total", 0.0)
        val totalString = String.format("%.0f", total)
        txtTongTien.text = total.toString()



        btnThanhToan.setOnClickListener {
            Toast.makeText(this, "đã click", Toast.LENGTH_SHORT).show()
            val orderApi = CreateOrder()
            try {
                val data: JSONObject = orderApi.createOrder(totalString)
                Log.d("TAG_API_RESPONSE", "Response from API: $data") // Log phản hồi API
                val code = data.optString("returncode", "") // Giá trị mặc định là ""
                if (code == "1") {
                    val token = data.optString("zptranstoken", "")
                    if (token.isNotEmpty()) {
                        ZaloPaySDK.getInstance().payOrder(
                            this@OrderPaymentActivity, token, "demozpdk://app",
                            object : PayOrderListener {
                                override fun onPaymentSucceeded(s: String?, s1: String?, s2: String?) {
                                    val intent1 = Intent(
                                        this@OrderPaymentActivity,
                                        PaymentNotificationActivity::class.java
                                    )
                                    intent1.putExtra("result", "Thanh toán thành công")
                                    startActivity(intent1)
                                }

                                override fun onPaymentCanceled(s: String?, s1: String?) {
                                    val intent1 = Intent(
                                        this@OrderPaymentActivity,
                                        PaymentNotificationActivity::class.java
                                    )
                                    intent1.putExtra("result", "Hủy thanh toán")
                                    startActivity(intent1)
                                }

                                override fun onPaymentError(
                                    error: ZaloPayError?,
                                    s: String?,
                                    s1: String?
                                ) {
                                    val intent1 = Intent(
                                        this@OrderPaymentActivity,
                                        PaymentNotificationActivity::class.java
                                    )
                                    intent1.putExtra("result", "Lỗi thanh toán")
                                    startActivity(intent1)
                                }
                            }
                        )
                    } else {
                        Log.e("TAG_API_RESPONSE", "Token không tồn tại hoặc rỗng")
                    }
                } else {
                    Log.e("TAG_API_RESPONSE", "Invalid returncode: $code")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("TAG_API_RESPONSE", "Error: ${e.message}")
            }
        }


    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("TAG_API_RESPONSE", "Received intent: $intent")
        ZaloPaySDK.getInstance().onResult(intent)
    }


}