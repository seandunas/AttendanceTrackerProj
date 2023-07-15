package com.example.attendancetracker
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
//import com.google.zxing.BarcodeFormat
//import com.google.zxing.Result
//import com.google.zxing.common.HybridBinarizer
//import com.google.zxing.qrcode.QRCodeReader
////import com.google.zxing.integration.android.IntentIntegrator //scanner
import android.content.pm.ActivityInfo //scanner
import android.webkit.WebView //webview
import androidx.camera.core.ImageCapture
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.nio.ByteBuffer

class WelcomeActivity: Activity() {
    lateinit var authuservalue: TextView
    lateinit var logoutBtn: Button
    private lateinit var webView: WebView //webview

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        webView = findViewById(R.id.webView) //webview
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //scanner

        authuservalue = findViewById(R.id.tvuser)
        logoutBtn = findViewById(R.id.logoutBtn)

        val sharedPref = getSharedPreferences("auth-user", MODE_PRIVATE)
        val userName = sharedPref.getString("auth-user", null)
        authuservalue.text = "Welcome \n$userName"

        val qrScannerBtn = findViewById<FrameLayout>(R.id.qrScannerContainer) //scanner
        qrScannerBtn.setOnClickListener {
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_AZTEC).enableAllPotentialBarcodes()
                .build()

            val scanner = BarcodeScanning.getClient()
            val imageCapture = ImageCapture.Builder()
            val image = InputImage.fromByteBuffer(
                ByteBuffer.allocate(20),
                /* image width */ 480,
                /* image height */ 360,
                0,
                InputImage.IMAGE_FORMAT_NV21 // or IMAGE_FORMAT_YV12
            )
            val result = scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    print("Success: ${barcodes.size}")
                }
                .addOnFailureListener {
                    print("FAIL")
                }
            // Initialize the QR scanner
//            val integrator = IntentIntegrator(this@WelcomeActivity)
//            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
//            integrator.setPrompt("Scan QR code")
//            integrator.setCameraId(0) // Use the rear camera
//            integrator.setBeepEnabled(false)
//            integrator.initiateScan()

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { //webview
        super.onActivityResult(requestCode, resultCode, data)

//        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//        if (result != null) {
//            if (result.contents != null) {
//                // QR code was successfully scanned
//                val scannedData = result.contents
//
//                // Load the URL into the WebView
//                webView.loadUrl(scannedData)
//            } else {
//                // QR code scanning was canceled or failed
//                // Handle the error or show a message to the user
//            }
//        }
    }
}
