package com.example.bookapp.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BookScreen (
    bookUiState: BookDetailUiState,
    retryAction: () -> Unit
) {
    when (bookUiState) {
        is BookDetailUiState.Loading -> LoadinScreen()
        is BookDetailUiState.Error -> ErrorScreen(retryAction =  retryAction)
        is BookDetailUiState.Success -> Text(text = "Current book: ${bookUiState.book.volumeInfo.title}")
    }
}