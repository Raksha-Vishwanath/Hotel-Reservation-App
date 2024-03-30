package com.example.myrak

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myrak.ui.home.HomeFragment

class thank: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thank)

        val roomtype = intent.getStringExtra("roomtype")
        val numofroom = intent.getStringExtra("numofroom")
        val date = intent.getStringExtra("date")

        val text2 = findViewById<TextView>(R.id.text2)
        val text3 = findViewById<TextView>(R.id.text3)
        val text4 = findViewById<TextView>(R.id.text4)

        text2.text="Room Type: $roomtype"
        text3.text="Number of Rooms: $numofroom"
        text4.text="Checkin Date: $date"

        val explicitBtn: Button = findViewById(R.id.btn_rtn)
        explicitBtn.setOnClickListener {
            val intent = Intent(this@thank, MainActivity::class.java)
            startActivity(intent)
        }
    }
}