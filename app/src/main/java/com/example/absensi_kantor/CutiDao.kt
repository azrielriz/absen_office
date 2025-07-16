//package com.example.absensi_kantor.room
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//
//@Dao
//interface CutiDao {
//    @Insert
//    suspend fun insertCuti(cuti: CutiEntity)
//
//    @Query("SELECT * FROM cuti ORDER BY id DESC")
//    suspend fun getAllCuti(): List<CutiEntity>
//
//    @Query("DELETE FROM cuti")
//    suspend fun deleteAll()
//}
