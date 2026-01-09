package com.example.questfirebase_032.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.questfirebase_032.repositori.RepositorySiswa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.questfirebase_032.model.DetailSiswa
import com.example.questfirebase_032.model.UIStateSiswa
import com.example.questfirebase_032.model.toDataSiswa
import com.example.questfirebase_032.model.toUiStateSiswa
import com.example.questfirebase_032.view.route.DestinasiDetail
import kotlinx.coroutines.launch

class EditViewModel(savedStateHandle: SavedStateHandle, private val repositorySiswa:
RepositorySiswa
) : ViewModel() {
    var uiStateSiswa by mutableStateOf(UIStateSiswa())
        private set

    private val idSiswa: String =
        savedStateHandle.get<String>(DestinasiDetail.itemIdArg)
            ?: error("idSiswa tidak ditemukan")

    init {
        viewModelScope.launch {
            uiStateSiswa = repositorySiswa.getSatuSiswa(idSiswa)!!
                .toUiStateSiswa(true)
        }
    }

    fun updateUiState(detailSiswa: DetailSiswa) {
        uiStateSiswa =
            UIStateSiswa(
                detailSiswa = detailSiswa, isEntryValid = validasiInput
                    (detailSiswa)
            )
    }

    private fun validasiInput(uiState: DetailSiswa = uiStateSiswa.detailSiswa) : Boolean {
        return with(uiState) {
            nama.isNotBlank() && alamat.isNotBlank() && telpon.isNotBlank()
        }
    }

