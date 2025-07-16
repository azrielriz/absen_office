package com.example.absensi_kantor

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.io.File
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.absensi_kantor.Riwayat.AbsenRiwayat

class RiwayatAdapter : ListAdapter<AbsenRiwayat, RiwayatAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tanggal: TextView = itemView.findViewById(R.id.textTanggal)
        val jam: TextView = itemView.findViewById(R.id.textJam)
        val status: TextView = itemView.findViewById(R.id.textStatus)
        val keteranganText: TextView = itemView.findViewById(R.id.keteranganText)
        val foto: ImageView = itemView.findViewById(R.id.imageFoto)

        // Lokasi
        val textLokasi: TextView = itemView.findViewById(R.id.textLokasi)
        val btnLihatLokasi: Button = itemView.findViewById(R.id.btnLihatLokasi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val riwayat = getItem(position)
        holder.tanggal.text = "Tanggal: ${riwayat.tanggal}"
        holder.jam.text = "Jam: ${riwayat.jam}"
        holder.status.text = "Status: ${riwayat.status}"
        holder.keteranganText.text = "Keterangan: ${riwayat.keterangan ?: "-"}"

        // Tampilkan foto jika ada
        if (!riwayat.fotoPath.isNullOrEmpty()) {
            val file = File(riwayat.fotoPath)
            if (file.exists()) {
                val bitmap = android.graphics.BitmapFactory.decodeFile(file.absolutePath)
                holder.foto.setImageBitmap(bitmap)
                holder.foto.visibility = View.VISIBLE
            } else {
                holder.foto.setImageDrawable(null)
                holder.foto.visibility = View.GONE
            }
        } else {
            holder.foto.setImageDrawable(null)
            holder.foto.visibility = View.GONE
        }

        // Tampilkan lokasi jika ada
        if (riwayat.latitude != null && riwayat.longitude != null) {
            holder.textLokasi.text = "Lokasi: ${riwayat.latitude}, ${riwayat.longitude}"
            holder.btnLihatLokasi.visibility = View.VISIBLE

            holder.btnLihatLokasi.setOnClickListener {
                val gmapsUrl = "https://www.google.com/maps?q=${riwayat.latitude},${riwayat.longitude}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(gmapsUrl))
                intent.setPackage("com.google.android.apps.maps") // Prefer Maps App if available
                holder.itemView.context.startActivity(intent)
            }
        } else {
            holder.textLokasi.text = "Lokasi: -"
            holder.btnLihatLokasi.visibility = View.GONE
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AbsenRiwayat>() {
        override fun areItemsTheSame(oldItem: AbsenRiwayat, newItem: AbsenRiwayat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AbsenRiwayat, newItem: AbsenRiwayat): Boolean {
            return oldItem == newItem
        }
    }
}
