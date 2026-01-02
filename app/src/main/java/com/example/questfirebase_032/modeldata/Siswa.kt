package com.example.questfirebase_032.model

data class Siswa(
    val id: String = "", // Diganti dari Long ke String agar cocok dengan Firestore
    val nama: String = "",
    val alamat: String = "",
    val telpon: String = ""
)

data class DetailSiswa(
    val id: String = "",
    val nama: String = "",
    val alamat: String = "",
    val telpon: String = ""
)

/* Fungsi konversi dari DetailSiswa ke Siswa */
fun DetailSiswa.toDataSiswa(): Siswa = Siswa(
    id = id,
    nama = nama,
    alamat = alamat,
    telpon = telpon
)

/* Fungsi konversi dari Siswa ke DetailSiswa */
fun Siswa.toDetailSiswa(): DetailSiswa = DetailSiswa(
    id = id,
    nama = nama,
    alamat = alamat,
    telpon = telpon
)

data class UIStateSiswa(
    val detailSiswa: DetailSiswa = DetailSiswa(),
    val isEntryValid: Boolean = false
)

fun Siswa.toUiStateSiswa(isEntryValid: Boolean = false): UIStateSiswa = UIStateSiswa(
    detailSiswa = this.toDetailSiswa(),
    isEntryValid = isEntryValid
)