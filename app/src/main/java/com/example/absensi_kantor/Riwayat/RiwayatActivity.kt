package com.example.absensi_kantor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.absensi_kantor.database.AppDatabase
import kotlinx.coroutines.launch

class RiwayatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RiwayatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        recyclerView = findViewById(R.id.recyclerViewRiwayat)
        adapter = RiwayatAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val database = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val riwayatList = database.absenRiwayatDao().getAllRiwayat()
            adapter.submitList(riwayatList)
        }
    }
}