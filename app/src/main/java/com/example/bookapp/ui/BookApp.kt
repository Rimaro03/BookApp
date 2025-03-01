package com.example.bookapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookapp.ui.screen.BookViewModel
import com.example.bookapp.ui.screen.VolumeListUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookApp() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        // topBar
    ) { innerPadding ->
        Surface {
            val bookViewModel: BookViewModel = viewModel(factory = BookViewModel.factory)
            val volumeListUiState = bookViewModel.volumeListUiState
            when(volumeListUiState) {
                is VolumeListUiState.Loading -> Text(text = "Loading", modifier = Modifier.padding(innerPadding))
                is VolumeListUiState.Error -> Text(text = "Error", modifier = Modifier.padding(innerPadding))
                is VolumeListUiState.Success ->
                    Text(text = "${volumeListUiState.books.size} books retrieved",
                        modifier = Modifier.padding(innerPadding)
                    )
            }
        }
    }
}