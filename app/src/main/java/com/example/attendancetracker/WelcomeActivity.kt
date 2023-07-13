package com.example.attendancetracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class WelcomeActivity : ComponentActivity() {
    lateinit var authuservalue: TextView
    lateinit var logoutBtn: Button

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        authuservalue = findViewById(R.id.tvuser)
        logoutBtn = findViewById(R.id.logoutBtn)

        val sharedPref = getSharedPreferences("auth-user", MODE_PRIVATE)
        val userName = sharedPref.getString("auth-user", null)
        authuservalue.text = "Welcome \n$userName"

        logoutBtn.setOnClickListener {
            // Clear the shared preferences and navigate back to the login screen
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        /*
        val logmeout = findViewById<Button>(R.id.loginBtn) // set on-click listener
        logmeout.setOnClickListener {

            val intent = Intent(this@WelcomeActivity,MainActivity::class.java)
            startActivity(intent)
            this.finish()

        }
         */
    }
}