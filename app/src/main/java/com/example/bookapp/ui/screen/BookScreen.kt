package com.example.bookapp.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BookScreen (
    bookUiState: BookDetailUiState
) {
    when (bookUiState) {
        is BookDetailUiState.Loading -> Text(text = "Loading")
        is BookDetailUiState.Error -> Text(text = "Error")
        is BookDetailUiState.Success -> Text(text = "Current book: ${bookUiState.book.volumeInfo.title}")
    }
}