package com.example.absensi_kantor

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.absensi_kantor.dao.UserDao
import com.example.absensi_kantor.database.AppDatabase
import com.example.absensi_kantor.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pastikan import ke AppDatabase sudah benar
        userDao = AppDatabase.getDatabase(this).userDao()

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    // login() function di DAO harus dibuat
                    val user = userDao.login(username, password)
                    if (user != null) {
                        Toast.makeText(this@LoginActivity, "Login Sukses", Toast.LENGTH_SHORT).show()

                        val preferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        preferences.edit().putString("username", username).apply()

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Username atau Password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
