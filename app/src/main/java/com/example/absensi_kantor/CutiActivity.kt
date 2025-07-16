package com.example.absensi_kantor

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.absensi_kantor.databinding.ActivityCutiBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*

class CutiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCutiBinding
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCutiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences("cuti_prefs", MODE_PRIVATE)

        binding.btnAjukanCuti.setOnClickListener {
            val nama = binding.editNama.text.toString().trim()
            val tanggalMulai = binding.tanggalMulaiEdit.text.toString().trim()
            val tanggalSelesai = binding.tanggalSelesaiEdit.text.toString().trim()
            val alasan = binding.alasanEdit.text.toString().trim()
            val jumlahHari = binding.editJumlahHari.text.toString().toIntOrNull() ?: 0

            // Validasi
            if (nama.isEmpty() || tanggalMulai.isEmpty() || tanggalSelesai.isEmpty() || alasan.isEmpty() || jumlahHari <= 0) {
                Toast.makeText(this, "Mohon lengkapi semua field dengan benar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (jumlahHari > 12) {
                Toast.makeText(this, "Jumlah cuti tidak boleh melebihi 12 hari", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kirim ke server
            kirimCutiKeLaravel(tanggalMulai, tanggalSelesai, alasan, jumlahHari)


            // Simpan lokal ke SharedPreferences
            val rangeTanggal = "$tanggalMulai - $tanggalSelesai"
            val newCuti = CutiLaporan(nama, rangeTanggal, alasan, jumlahHari)

            val json = sharedPrefs.getString("laporan_cuti", "[]")
            val type = object : TypeToken<MutableList<CutiLaporan>>() {}.type
            val list: MutableList<CutiLaporan> = Gson().fromJson(json, type)

            list.add(newCuti)
            sharedPrefs.edit().putString("laporan_cuti", Gson().toJson(list)).apply()

            Toast.makeText(this, "Cuti berhasil diajukan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun kirimCutiKeLaravel(tanggalMulai: String, tanggalSelesai: String, alasan: String, jumlahHari: Int) {
        val preferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val username = preferences.getString("username", "Guest") ?: "Guest"

        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("tanggal_mulai", tanggalMulai)
            .addFormDataPart("tanggal_selesai", tanggalSelesai)
            .addFormDataPart("alasan", alasan)
            .addFormDataPart("jumlah_hari", jumlahHari.toString())
            .build()

        val request = Request.Builder()
            .url("http://10.0.2.2:8000/api/cuti") // pastikan route ini aktif
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                Log.e("CutiDebug", "Gagal koneksi: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@CutiActivity, "Gagal kirim cuti ke server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body?.string()
                Log.d("CutiDebug", "Kode respons: ${response.code}")
                Log.d("CutiDebug", "Isi respons: $responseText")
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CutiActivity, "Cuti berhasil dikirim ke server", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@CutiActivity, "Gagal simpan cuti ke server", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
