package register

import HomeActivity.HomeActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.latihan.nasholearn.R
import com.latihan.nasholearn.databinding.ActivityRegisterBinding
import com.latihan.nasholearn.login.LoginActivity
import com.google.firebase.database.DatabaseReference   //harus import kalo ada yg berkaitan dengan database
import com.google.firebase.database.FirebaseDatabase    //harus import kalo ada yg berkaitan dengan database
import com.latihan.nasholearn.DataRegis.Regis
import android.text.Editable //tambahan apabila harus memberi validasi
import android.text.TextWatcher //tambahan apabila harus memberi validasi
import android.util.Patterns //tambahan apabila harus memberi validasi



//versi sendiri namun belum ada validasi textField

//class RegisterActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityRegisterBinding
//    private lateinit var database: DatabaseReference
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityRegisterBinding.inflate(layoutInflater)
////        enableEdgeToEdge()
//        setContentView(binding.root)
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
////            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
////            insets
//
//        database = FirebaseDatabase.getInstance().getReference("Akun2")
//
//        binding.RBtn1.setOnClickListener {
//            val fullName = binding.edtFullname.text.toString()
//            val email = binding.edtEmail.text.toString()
//            val password = binding.edtPassword.text.toString()
//            val confPassword = binding.edtConfPassword.text.toString()
//
//            if (validateInput(fullName, email, password, confPassword)) {
//                binding.RBtn1.isEnabled = true
//                registerUser(fullName, email, password)
//            }
//        }
//        binding.txtLogin.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }
//    }
//
//    private fun validateInput(fullName: String, email: String, password: String, confPassword: String): Boolean {
//        if (fullName.isEmpty()) {
//            Toast.makeText(this, "Full Name is required", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        if (email.isEmpty()) {
//            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        if (password.isEmpty()) {
//            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        if (password != confPassword) {
//            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        return true
//    }
//
//    private fun registerUser(fullName: String, email: String, password: String) {
//        val userId = database.push().key // Generate unique user ID
//
//        if (userId != null) {
//            val user = Regis(id = userId, fullName = fullName, email = email, password = password)
//
//            database.child(userId).setValue(user).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, LoginActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    Toast.makeText(this, "Failed to register user", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//}

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Akun2")

        // Awalnya nonaktifkan tombol Register
        binding.RBtn1.isEnabled = false

        // Validasi real-time untuk Full Name
        binding.edtFullname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateFullName()
                updateButtonState() // Cek validasi untuk enable/disable tombol
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Validasi real-time untuk Email
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateEmail()
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Validasi real-time untuk Password
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validatePassword()
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Validasi real-time untuk Konfirmasi Password
        binding.edtConfPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateConfPassword()
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Action untuk tombol Register
        binding.RBtn1.setOnClickListener {
            val fullName = binding.edtFullname.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            registerUser(fullName, email, password)
        }

        binding.txtLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.backRegister.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Validasi Full Name minimal 2 kata
    private fun validateFullName(): Boolean {
        val fullName = binding.edtFullname.text.toString()
        return if (fullName.trim().split("\\s+".toRegex()).size < 2) {
            binding.edtFullname.error = "Nama harus berisikan minimal 2 kata"
            false
        } else {
            binding.edtFullname.error = null
            true
        }
    }

    // Validasi Email format
    private fun validateEmail(): Boolean {
        val email = binding.edtEmail.text.toString()
        return if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Email tidak valid"
            false
        } else {
            binding.edtEmail.error = null
            true
        }
    }

    // Validasi Password minimal 8 karakter
    private fun validatePassword(): Boolean {
        val password = binding.edtPassword.text.toString()
        return if (password.length < 8) {
            binding.edtPassword.error = "Password harus 8 karakter"
            false
        } else {
            binding.edtPassword.error = null
            true
        }
    }

    // Validasi Konfirmasi Password sama dengan Password
    private fun validateConfPassword(): Boolean {
        val password = binding.edtPassword.text.toString()
        val confPassword = binding.edtConfPassword.text.toString()
        return if (password != confPassword) {
            binding.edtConfPassword.error = "Password harus sama"
            false
        } else {
            binding.edtConfPassword.error = null
            true
        }
    }

    // Cek apakah semua input valid dan aktifkan tombol Register
    private fun updateButtonState() {
        binding.RBtn1.isEnabled = validateFullName() && validateEmail() && validatePassword() && validateConfPassword()
    }

    // Fungsi untuk mendaftarkan pengguna
    private fun registerUser(fullName: String, email: String, password: String) {
        val userId = database.push().key // Generate unique user ID

        if (userId != null) {
            val user = Regis(id = userId, fullName = fullName, email = email, password = password)

            database.child(userId).setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Failed to register user", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


