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

class FirebaseRepositorySiswa : RepositorySiswa {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("siswa")

    override suspend fun getDataSiswa(): List<Siswa> {
        return try {
            collection.get().await().documents.map { doc ->
                Siswa(
                    id = doc.getString("id") ?: "", // Mengambil id sebagai String
                    nama = doc.getString("nama") ?: "",
                    alamat = doc.getString("alamat") ?: "",
                    telpon = doc.getString("telpon") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    override suspend fun postDataSiswa(siswa: Siswa) {
        val docRef = if (siswa.id.isEmpty()) collection.document() else collection.document(siswa.id)

        val data = hashMapOf(
            "id" to docRef.id, // Menyimpan document ID yang asli (String)
            "nama" to siswa.nama,
            "alamat" to siswa.alamat,
            "telpon" to siswa.telpon
        )

        docRef.set(data).await()
    }

    // Implementasi delete (opsional, tapi dibutuhkan oleh HalamanHome yang tadi disalin)
    override suspend fun deleteSiswa(siswa: Siswa) {
        try {
            collection.document(siswa.id).delete().await()
        } catch (e: Exception) {
            throw Exception("Gagal menghapus data siswa: ${e.message}")
        }
    }
}