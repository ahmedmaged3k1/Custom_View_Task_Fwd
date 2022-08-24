package com.udacity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class DetailActivity : AppCompatActivity() {
    private lateinit var button: AppCompatButton
    private lateinit var name: TextView
    private lateinit var status: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        button = findViewById(R.id.okButton)
        status = findViewById(R.id.textView2)
        name = findViewById(R.id.textView3)

        status.text =   intent.getStringExtra("status")
         name.text =   intent.getStringExtra("File")


        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

}
