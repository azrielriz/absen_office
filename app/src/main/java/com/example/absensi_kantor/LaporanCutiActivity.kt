package com.example.absensi_kantor

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.absensi_kantor.databinding.ActivityLaporanCutiBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.recyclerview.widget.LinearLayoutManager

class LaporanCutiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaporanCutiBinding
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var adapter: CutiAdapter // Adapter untuk RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporanCutiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences("cuti_prefs", MODE_PRIVATE)

        // Ambil data dari SharedPreferences
        val json = sharedPrefs.getString("laporan_cuti", "[]")
        val type = object : TypeToken<MutableList<CutiLaporan>>() {}.type
        val list: MutableList<CutiLaporan> = Gson().fromJson(json, type)

        // Set ke RecyclerView
        adapter = CutiAdapter(list)
        binding.recyclerViewCuti.adapter = adapter
        binding.recyclerViewCuti.layoutManager = LinearLayoutManager(this)
    }
}

