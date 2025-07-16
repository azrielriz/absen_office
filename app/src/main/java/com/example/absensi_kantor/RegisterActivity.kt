package com.example.absensi_kantor

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.absensi_kantor.dao.UserDao
import com.example.absensi_kantor.database.AppDatabase
import com.example.absensi_kantor.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = AppDatabase.getDatabase(this).userDao()

        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val jabatan = binding.jabatanEditText.text.toString().trim()
            val nohp = binding.nohpEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty() && jabatan.isNotEmpty() && nohp.isNotEmpty()) {
                lifecycleScope.launch {
                    userDao.insert(User(username = username, password = password, email = email, jabatan = jabatan, nohp = nohp))
                    Toast.makeText(this@RegisterActivity, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}