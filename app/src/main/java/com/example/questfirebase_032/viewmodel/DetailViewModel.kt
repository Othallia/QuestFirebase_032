package com.example.questfirebase_032.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.questfirebase_032.model.Siswa
import com.example.questfirebase_032.repositori.RepositorySiswa
import com.example.questfirebase_032.view.route.DestinasiDetail
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    data class Success(val siswa: Siswa) : DetailUiState
    object Error : DetailUiState
    object Loading : DetailUiState
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositorySiswa: RepositorySiswa
) : ViewModel() {

    // PERBAIKAN DI SINI: Gunakan DestinasiDetail.itemIdArg
    private val idSiswa: String = checkNotNull(savedStateHandle[DestinasiDetail.itemIdArg])

    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    init {
        getSatuSiswa()
    }

    fun getSatuSiswa() {
        viewModelScope.launch {
            detailUiState = DetailUiState.Loading
            detailUiState = try {
                val siswa = repositorySiswa.getSatuSiswa(idSiswa)
                if (siswa != null) {
                    DetailUiState.Success(siswa)
                } else {
                    DetailUiState.Error
                }
            } catch (e: Exception) {
                DetailUiState.Error
            }
        }
    }

    fun hapusSatuSiswa() {
        viewModelScope.launch {
            try {
                repositorySiswa.hapusSatuSiswa(idSiswa)
            } catch (e: Exception) {
                println("Gagal menghapus: ${e.message}")
            }
        }
    }
}

