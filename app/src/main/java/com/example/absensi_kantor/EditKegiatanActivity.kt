package com.example.absensi_kantor

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.absensi_kantor.databinding.ActivityEditKegiatanBinding
import com.google.gson.Gson

class EditKegiatanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditKegiatanBinding
    private lateinit var kegiatan: Kegiatan
    private var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditKegiatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataJson = intent.getStringExtra("kegiatan")
        index = intent.getIntExtra("index", -1)
        kegiatan = Gson().fromJson(dataJson, Kegiatan::class.java)

        binding.editJudul.setText(kegiatan.judul)
        binding.editTanggal.setText(kegiatan.tanggal)
        binding.editKeterangan.setText(kegiatan.keterangan)

        binding.btnSimpanKegiatan.setOnClickListener {
            kegiatan.judul = binding.editJudul.text.toString()
            kegiatan.tanggal = binding.editTanggal.text.toString()
            kegiatan.keterangan = binding.editKeterangan.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("kegiatan_result", Gson().toJson(kegiatan))
            resultIntent.putExtra("index_result", index)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
