package com.example.questfirebase_032.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.questfirebase_032.R
import com.example.questfirebase_032.model.Siswa
import com.example.questfirebase_032.view.route.DestinasiDetail
import com.example.questfirebase_032.viewmodel.DetailUiState
import com.example.questfirebase_032.viewmodel.DetailViewModel
import com.example.questfirebase_032.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSiswaScreen(
    navigateToEditItem: (String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.detailUiState // Gunakan detailUiState, BUKAN statusUIDetail

    Scaffold(
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiDetail.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            if (uiState is DetailUiState.Success) {
                FloatingActionButton(
                    onClick = { navigateToEditItem(uiState.siswa.id) },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit_siswa)
                    )
                }
            }
        },
    ) { innerPadding ->
        when (uiState) {
            is DetailUiState.Success -> {
                ItemDetailBody(
                    siswa = uiState.siswa,
                    onDelete = {
                        viewModel.hapusSatuSiswa()
                        navigateBack()
                    },
                    modifier = modifier.padding(innerPadding)
                )
            }
            is DetailUiState.Loading -> OnLoading(modifier = modifier.padding(innerPadding))
            is DetailUiState.Error -> OnError(retryAction = { viewModel.getSatuSiswa() }, modifier = modifier.padding(innerPadding))
        }
    }
}

@Composable
fun ItemDetailBody(
    siswa: Siswa,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ItemDetail(siswa = siswa)
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false }
            )
        }
    }
}

@Composable
fun ItemDetail(siswa: Siswa, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            ItemDetailRow(labelResID = R.string.nama, itemDetail = siswa.nama)
            ItemDetailRow(labelResID = R.string.alamat, itemDetail = siswa.alamat)
            ItemDetailRow(labelResID = R.string.telpon, itemDetail = siswa.telpon)
        }
    }
}

@Composable
fun ItemDetailRow(labelResID: Int, itemDetail: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = itemDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) { Text(text = stringResource(R.string.no)) }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) { Text(text = stringResource(R.string.yes)) }
        }
    )
}
