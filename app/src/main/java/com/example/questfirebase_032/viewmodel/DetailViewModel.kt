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

