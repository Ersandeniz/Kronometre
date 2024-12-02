package com.example.kronometre

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kronometre.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this , Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                requestPermission()
            }else{
                Intent(this ,KronometreService::class.java ).apply {
                    startService(this)
                }
            }
        }
        binding.btnStop.setOnClickListener {
            Intent(this ,KronometreService::class.java ).apply {
                stopService(this)
            }
        }


    }
    private fun requestPermission()= ActivityCompat.requestPermissions(this , arrayOf(Manifest.permission.POST_NOTIFICATIONS),101)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==101&&grantResults.isNotEmpty()) {
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                Intent(this ,KronometreService::class.java ).apply {
                    startService(this)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}