package com.example.questfirebase_032.repositori

import com.example.questfirebase_032.model.Siswa
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface RepositorySiswa {
    suspend fun getDataSiswa(): List<Siswa>
    suspend fun postDataSiswa(siswa: Siswa)
    // Tambahkan fungsi delete jika diperlukan untuk fitur hapus di Home
    suspend fun deleteSiswa(siswa: Siswa)
}
