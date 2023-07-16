package com.example.attendancetracker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.google.zxing.integration.android.IntentIntegrator //scanner
import android.content.pm.ActivityInfo //scanner
import android.webkit.WebView //webview
import android.widget.Toast
import android.util.Log

class WelcomeActivity: Activity() {
    private lateinit var authuservalue: TextView
    private lateinit var logoutBtn: Button
    private lateinit var webView: WebView //webview
    private var userName: String? = null

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId", "SetTextI18n",
        "SourceLockedOrientationActivity"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        webView = findViewById(R.id.webView) //webview
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //scanner

        authuservalue = findViewById(R.id.tvuser)
        logoutBtn = findViewById(R.id.logoutBtn)

        val sharedPref = getSharedPreferences("auth-user", MODE_PRIVATE)
        userName = sharedPref.getString("auth-user", null)
        authuservalue.text = "Welcome \n$userName"

        val qrScannerBtn = findViewById<FrameLayout>(R.id.qrScannerContainer) //scanner
        qrScannerBtn.setOnClickListener {
            // Initialize the QR scanner
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val integrator = IntentIntegrator(this@WelcomeActivity)
            integrator.setOrientationLocked(true)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setPrompt("Scan QR code")
            integrator.setCameraId(0) // Use the rear camera
            integrator.setBeepEnabled(false)
            integrator.initiateScan()

            Log.d("TAG", "message")
        }

        logoutBtn.setOnClickListener {
            // Clear the shared preferences and navigate back to the login screen
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { //web-view
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                // QR code was successfully scanned
                val scannedData = result.contents
                // Load the URL into the WebView
                Log.d("WEBVIEW LOADED THIS: ", result.contents)

                if(result.contents.equals("https://me-qr.com/A4FFbgrv")){
                    webView.loadUrl("https://192.168.100.3/PHP/myit0079db/attendance.php/?email=anjo@gmail.com&userId=1")
                }
            } else {
                // QR code scanning was canceled or failed
                // Handle the error or show a message to the user
            }
        }
    }
}
