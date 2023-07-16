package com.example.attendancetracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.util.Log

class MainActivity : ComponentActivity() {
    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailInput = findViewById(R.id.etemail)
        passwordInput= findViewById(R.id.etpassword)
        // set on-click listener
        val btn_logme_in = findViewById<Button>(R.id.loginBtn)
        btn_logme_in.setOnClickListener {
            Toast.makeText(this@MainActivity, "Logging-in.", Toast.LENGTH_SHORT).show()

            val queue = Volley.newRequestQueue(this.applicationContext)
            val url = "https://192.168.100.3/PHP/myit0079db/login.php" // Replace with the URL of your login API endpoint

            val sharedPref = getSharedPreferences("auth-user", MODE_PRIVATE)
            val editor = sharedPref.edit()

            editor.apply {
                putString("auth-user", emailInput.text.toString())
                apply()
            }

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    if (response.trim() == "success") {
                        // Login successful

                        Log.d("STRING LOGIN SUCCESSFUL",response)
                        val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this@MainActivity, "Login successful", Toast.LENGTH_SHORT).show()

                        // Proceed to the next activity after successful login
                    } else {
                        // Login failed
                        Toast.makeText(this@MainActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    // Handle error
                    Toast.makeText(this@MainActivity, "Login error: " + error.message, Toast.LENGTH_SHORT).show()
                }) {

                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["email"] = findViewById<EditText>(R.id.etemail).text.toString()
                    params["password"] = findViewById<EditText>(R.id.etpassword).text.toString()
                    params["email"]?.let { it1 -> Log.d("PARAMS VALUE", it1) }
                    return params
                }
            }

            NukeSSLCerts.nuke()
            queue.add(stringRequest)
        }

        val registerMe = findViewById<TextView>(R.id.tvregs) //go to RegisterActivity on click
        registerMe.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}