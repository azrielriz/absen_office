package com.example.absensi_kantor.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.absensi_kantor.Riwayat.AbsenRiwayat

@Dao
interface AbsenRiwayatDao {
    @Insert
    suspend fun insert(riwayat: AbsenRiwayat)

    @Query("SELECT * FROM absen_riwayat ORDER BY id DESC")
    suspend fun getAllRiwayat(): List<AbsenRiwayat>
}