package com.latihan.nasholearn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.latihan.nasholearn.databinding.ActivityMainBinding
import com.latihan.nasholearn.login.LoginActivity
import register.RegisterActivity


class MainActivity : AppCompatActivity(){ //View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

//        enableEdgeToEdge()
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets

//        val btn_1: Button = findViewById(R.id.btn_1)
//        btn_1.setOnClickListener(this)
//
//        val btn_2: Button = findViewById(R.id.btn_2)
//        btn_2.setOnClickListener(this)
//
////        }

        binding.btn1.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btn2.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
//
//    override fun onClick(v: View?) {
//        when (v?.id) {
//            R.id.btn_1 -> {
//                val moveIntent = Intent(this@MainActivity, LoginActivity::class.java)
//                startActivity(moveIntent)
//            }
//
//            R.id.btn_2 -> {
//                val  moveIntent = Intent(this@MainActivity, activity_register::class.java)
//                startActivity(moveIntent)
//            }
//        }
//    }
//
//
}