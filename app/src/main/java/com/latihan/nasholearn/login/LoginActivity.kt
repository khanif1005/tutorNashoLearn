package com.latihan.nasholearn.login

import HomeActivity.HomeActivity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.latihan.nasholearn.databinding.ActivityLoginBinding
import register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.LBtn1.isEnabled = true

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Akun2")

        val imel = binding.edtEmail.text.toString()
        binding.edtEmail.doOnTextChanged { text, start, before, count ->
            if(!Patterns.EMAIL_ADDRESS.matcher(imel).matches()){
                binding.edtEmail.error = "Email tidak sesuai dengan format "
            } else {
                binding.edtEmail.error = null
            }
        }

        // Button login action ini
        binding.LBtn1.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtConfPassword.text.toString()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }

        // Button to open RegisterActivity
        binding.txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        // Validasi input email dan password tidak boleh kosong
        if (email.isEmpty()) {
            binding.edtEmail.error = "Email harus diisi"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Format email tidak valid"
            return false
        }
        if (password.isEmpty()) {
            binding.edtConfPassword.error = "Password harus diisi"
            return false
        }
        if (password.length < 8) {
            binding.edtConfPassword.error = "Password harus berisi minimal 8 karakter"
            return false
        }
        return true
    }

    private fun loginUser(email: String, password: String) {
        // Query untuk mencocokkan email dan password di Firebase Realtime Database
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userFound = false

                for (userSnapshot in snapshot.children) {
                    val userEmail = userSnapshot.child("email").getValue(String::class.java)
                    val userPassword = userSnapshot.child("password").getValue(String::class.java)

                    if (userEmail == email && userPassword == password) {
                        userFound = true
                        Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()

                        // Berpindah ke HomeActivity jika login berhasil
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish() // Menutup LoginActivity
                        break
                    }
                }

                if (!userFound) {
                    // Jika email dan password tidak cocok
                    Toast.makeText(this@LoginActivity, "Email atau password salah", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Jika terjadi kesalahan dalam membaca database
                Toast.makeText(this@LoginActivity, "Terjadi kesalahan saat login", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
