package com.example.questfirebase_032.repositori

import com.example.questfirebase_032.model.Siswa
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface RepositorySiswa {
    suspend fun getDataSiswa(): List<Siswa>
    suspend fun postDataSiswa(siswa: Siswa)
    suspend fun getSatuSiswa(id: String): Siswa? // Ubah Long ke String
    suspend fun editSatuSiswa(id: String, siswa: Siswa) // Ubah Long ke String
    suspend fun hapusSatuSiswa(id: String) // Ubah Long ke String
}

class FirebaseRepositorySiswa : RepositorySiswa {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("siswa")

    override suspend fun getDataSiswa(): List<Siswa> {
        return try {
            collection.get().await().documents.map { doc ->
                Siswa(
                    // Pastikan id diambil sebagai String
                    id = doc.getString("id") ?: "",
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
        // Logic baru: Jika ID kosong, buat ID baru dari Firestore. Jika ada, pakai yang lama.
        val docRef = if (siswa.id.isEmpty()) collection.document() else collection.document(siswa.id)

        // ID yang akan disimpan adalah ID dari docRef (unik string)
        val siswaId = if (siswa.id.isEmpty()) docRef.id else siswa.id

        val data = hashMapOf(
            "id" to siswaId,
            "nama" to siswa.nama,
            "alamat" to siswa.alamat,
            "telpon" to siswa.telpon
        )
        // Gunakan set agar ID dokumen di firestore sama dengan ID di dalam field datanya
        docRef.set(data).await()
    }

    override suspend fun getSatuSiswa(id: String): Siswa? { // Parameter jadi String
        return try {
            val query = collection.whereEqualTo("id", id).get().await()
            query.documents.firstOrNull()?.let { doc ->
                Siswa(
                    // Konsisten ambil String
                    id = doc.getString("id") ?: "",
                    nama = doc.getString("nama") ?: "",
                    alamat = doc.getString("alamat") ?: "",
                    telpon = doc.getString("telpon") ?: ""
                )
            }
        } catch (e: Exception) {
            println("Gagal baca data siswa : ${e.message}")
            null
        }
    }

    override suspend fun editSatuSiswa(id: String, siswa: Siswa) { // Parameter jadi String
        val docQuery = collection.whereEqualTo("id", id).get().await()
        val docId = docQuery.documents.firstOrNull()?.id ?: return

        collection.document(docId).set(
            mapOf(
                "id" to id, // Pastikan ID tetap konsisten
                "nama" to siswa.nama,
                "alamat" to siswa.alamat,
                "telpon" to siswa.telpon
            )
        ).await()
    }

    override suspend fun hapusSatuSiswa(id: String) { // Parameter jadi String
        val docQuery = collection.whereEqualTo("id", id).get().await()
        val docId = docQuery.documents.firstOrNull()?.id ?: return
        collection.document(docId).delete().await()
    }
}