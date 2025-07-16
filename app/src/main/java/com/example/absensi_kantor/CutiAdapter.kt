package com.example.absensi_kantor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CutiAdapter(private val items: List<CutiLaporan>) :
    RecyclerView.Adapter<CutiAdapter.CutiViewHolder>() {

    inner class CutiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaText = itemView.findViewById<TextView>(R.id.textNama)
        val tanggalText = itemView.findViewById<TextView>(R.id.textTanggal)
        val alasanText = itemView.findViewById<TextView>(R.id.textAlasan)
        val jumlahHariText = itemView.findViewById<TextView>(R.id.textJumlahHari)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CutiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cuti, parent, false)
        return CutiViewHolder(view)
    }

    override fun onBindViewHolder(holder: CutiViewHolder, position: Int) {
        val item = items[position]
        holder.namaText.text = item.nama
        holder.tanggalText.text = item.rangeTanggal
        holder.alasanText.text = item.alasan
        holder.jumlahHariText.text = "${item.jumlahHari} hari"
    }

    override fun getItemCount(): Int = items.size
}
