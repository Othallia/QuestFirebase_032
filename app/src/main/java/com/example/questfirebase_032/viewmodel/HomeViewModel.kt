package com.example.questfirebase_032.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.questfirebase_032.model.Siswa
import com.example.questfirebase_032.repositori.RepositorySiswa
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface HomeUiState {
    data class Success(val siswa: List<Siswa>) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel(private val repositorySiswa: RepositorySiswa) : ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getSiswa()
    }

    fun getSiswa() {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            homeUiState = try {
                HomeUiState.Success(repositorySiswa.getDataSiswa())
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: Exception) {
                HomeUiState.Error
            }
        }
    }

    fun deleteSiswa(siswa: Siswa) {
        viewModelScope.launch {
            try {
                // Gunakan hapusSatuSiswa dan ambil ID-nya saja
                repositorySiswa.hapusSatuSiswa(siswa.id)
                getSiswa()
            } catch (e: Exception) {
                homeUiState = HomeUiState.Error
            }
        }
    }
}