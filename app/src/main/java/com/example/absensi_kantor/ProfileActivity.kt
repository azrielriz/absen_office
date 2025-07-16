package com.example.absensi_kantor

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.absensi_kantor.dao.UserDao
import com.example.absensi_kantor.database.AppDatabase
import com.example.absensi_kantor.databinding.ActivityProfileBinding
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.File
import java.io.FileOutputStream


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var userDao: UserDao
    private lateinit var currentUsername: String

    companion object {
        private const val PICK_IMAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = AppDatabase.getDatabase(this).userDao()

        val preferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        currentUsername = preferences.getString("username", "Guest") ?: "Guest"
        binding.usernameTextView.text = currentUsername

        binding.btnChangeUsername.setOnClickListener {
            val newUsername = binding.newUsernameEditText.text.toString().trim()
            if (newUsername.isNotEmpty()) {
                lifecycleScope.launch {
                    userDao.updateUsername(currentUsername, newUsername)
                    preferences.edit().putString("username", newUsername).apply()
                    currentUsername = newUsername
                    binding.usernameTextView.text = newUsername
                    Toast.makeText(this@ProfileActivity, "Username berhasil diubah", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Username baru tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnChangePassword.setOnClickListener {
            val newPassword = binding.newPasswordEditText.text.toString().trim()
            if (newPassword.isNotEmpty()) {
                lifecycleScope.launch {
                    userDao.updatePassword(currentUsername, newPassword)
                    Toast.makeText(this@ProfileActivity, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Password baru tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnChangeProfilePic.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }

        binding.btnLogout.setOnClickListener {
            preferences.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage: Uri? = data.data
            if (selectedImage != null) {
                val savedPath = saveImageToInternalStorage(selectedImage)

                if (savedPath != null) {
                    val bitmap = BitmapFactory.decodeFile(savedPath)
                    binding.imageProfile.setImageBitmap(bitmap)

                    // Simpan path ke SharedPreferences
                    val preferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    preferences.edit().putString("profile_pic_path", savedPath).apply()

                    Toast.makeText(this, "Foto Profile berhasil disimpan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal menyimpan foto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val fileName = "profile_${System.currentTimeMillis()}.jpg"
            val file = File(filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
