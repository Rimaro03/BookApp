package com.example.bookapp.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.BookRepository
import com.example.bookapp.models.Book
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.network.HttpException
import com.example.bookapp.BookApplication
import okio.IOException

sealed interface VolumeListUiState {
    data object Error: VolumeListUiState
    data object Loading: VolumeListUiState
    data class Success(val books: List<Book>): VolumeListUiState
}

sealed interface BookDetailUiState {
    data object Error: BookDetailUiState
    data object Loading: BookDetailUiState
    data class Success(val book: Book): BookDetailUiState
}

class BookViewModel (private val bookRepository: BookRepository): ViewModel() {
    var volumeListUiState: VolumeListUiState by mutableStateOf(VolumeListUiState.Loading)
    var bookDetailUiState: BookDetailUiState by mutableStateOf(BookDetailUiState.Loading)

    init {
        getVolumeList("lotr")
    }

    fun getVolumeList(query: String) {
        viewModelScope.launch {
            volumeListUiState = VolumeListUiState.Loading
            volumeListUiState = try {
                var bookList: List<Book> = bookRepository.listVolumes(query).items
                VolumeListUiState.Success(bookList)
            }
            catch (e: IOException) {
                VolumeListUiState.Error
            }
            catch(e: HttpException)
            {
                VolumeListUiState.Error
            }
        }
    }

    fun getBook(bookId: String) {
        viewModelScope.launch {
            bookDetailUiState = BookDetailUiState.Loading
            bookDetailUiState = try {
                var book: Book = bookRepository.getVolume(bookId)
                BookDetailUiState.Success(book)
            }
            catch (e: IOException) {
                BookDetailUiState.Error
            }
            catch(e: HttpException)
            {
                BookDetailUiState.Error
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookApplication)
                val bookRepository = application.container.bookRepository
                BookViewModel(bookRepository)
            }
        }
    }
}