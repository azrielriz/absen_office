package com.example.absensi_kantor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KegiatanAdapter(
    private var list: List<Kegiatan>,
    private val onClick: (Kegiatan) -> Unit
) : RecyclerView.Adapter<KegiatanAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val judul = view.findViewById<TextView>(R.id.textJudul)
        val tanggal = view.findViewById<TextView>(R.id.textTanggal)
        val keterangan = view.findViewById<TextView>(R.id.textKeterangan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kegiatan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kegiatan = list[position]
        holder.judul.text = kegiatan.judul
        holder.tanggal.text = kegiatan.tanggal
        holder.keterangan.text = kegiatan.keterangan
        holder.view.setOnClickListener { onClick(kegiatan) }
    }

    fun updateData(newList: List<Kegiatan>) {
        list = newList
        notifyDataSetChanged()
    }
}
