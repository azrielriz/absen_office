package com.example.absensi_kantor.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.absensi_kantor.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("UPDATE users SET username = :newUsername WHERE username = :oldUsername")
    suspend fun updateUsername(oldUsername: String, newUsername: String)

    @Query("UPDATE users SET password = :newPassword WHERE username = :username")
    suspend fun updatePassword(username: String, newPassword: String)

}