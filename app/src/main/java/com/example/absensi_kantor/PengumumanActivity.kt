package com.example.absensi_kantor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.absensi_kantor.databinding.ActivityPengumumanBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PengumumanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPengumumanBinding
    private lateinit var adapter: KegiatanAdapter
    private var kegiatanList: MutableList<Kegiatan> = mutableListOf()

    companion object {
        const val REQUEST_EDIT_KEGIATAN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengumumanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()

        adapter = KegiatanAdapter(kegiatanList) { kegiatan ->
            val intent = Intent(this, EditKegiatanActivity::class.java)
            val index = kegiatanList.indexOf(kegiatan)
            intent.putExtra("kegiatan", Gson().toJson(kegiatan))
            intent.putExtra("index", index)
            startActivityForResult(intent, 100)
        }

        binding.recyclerPengumuman.adapter = adapter
    }

    private fun loadData() {
        val prefs = getSharedPreferences("kegiatan_prefs", MODE_PRIVATE)
        val json = prefs.getString("daftar_kegiatan", "[]")
        val listType = object : TypeToken<List<Kegiatan>>() {}.type
        kegiatanList = Gson().fromJson(json, listType)
    }

    private fun saveData() {
        val prefs = getSharedPreferences("kegiatan_prefs", MODE_PRIVATE)
        val json = Gson().toJson(kegiatanList)
        prefs.edit().putString("daftar_kegiatan", json).apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_KEGIATAN && resultCode == RESULT_OK && data != null) {
            val resultJson = data.getStringExtra("kegiatan_result")
            val index = data.getIntExtra("index_result", -1)

            if (resultJson != null && index != -1) {
                val updatedKegiatan = Gson().fromJson(resultJson, Kegiatan::class.java)

                // Perbarui data lokal dan simpan ulang ke prefs
                kegiatanList[index] = updatedKegiatan
                saveData()

                adapter.notifyItemChanged(index)
            }
        }
    }
}

