package com.example.bookapp.ui.screen

import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import okio.IOException

sealed interface VolumeListUiState {
    data object Error: VolumeListUiState
    data object Loading: VolumeListUiState
    data class Success(val books: Map<String, List<Book>>): VolumeListUiState
}

sealed interface BookDetailUiState {
    data object Error: BookDetailUiState
    data object Loading: BookDetailUiState
    data class Success(val book: Book, val bookDetail: Book): BookDetailUiState
}

sealed interface BookSearchUiState {
    data object Error: BookSearchUiState
    data object Loading: BookSearchUiState
    data class Success(val books: List<Book>): BookSearchUiState
}

class BookViewModel (private val bookRepository: BookRepository): ViewModel() {
    // Reason for mutableStateOf: compose watches the change of the value and recomposes the UI
    var volumeListUiState: VolumeListUiState by mutableStateOf(VolumeListUiState.Loading)
    var bookDetailUiState: BookDetailUiState by mutableStateOf(BookDetailUiState.Loading)
    var bookSearchUiState: BookSearchUiState by mutableStateOf(BookSearchUiState.Loading)
    val defaultQueries = listOf("Fiction", "History", "Lotr")

    init {
        getVolumeList()
    }

    fun getVolumeList(queries: List<String> = defaultQueries) {
        viewModelScope.launch {
            volumeListUiState = VolumeListUiState.Loading
            volumeListUiState = try {
                var books: MutableMap<String, List<Book>> = mutableMapOf()
                for(query in queries) {
                    Log.d("BookViewModel", query)
                    var bookList: List<Book> = bookRepository.listVolumes(query).items
                    books[query] = bookList
                }
                VolumeListUiState.Success(books)
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

    fun getBook(book: Book) {
        viewModelScope.launch {
            bookDetailUiState = BookDetailUiState.Loading
            bookDetailUiState = try {
                var bookDetail: Book = bookRepository.getVolume(book.id)
                BookDetailUiState.Success(
                    book = book,
                    bookDetail = bookDetail
                )
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

    fun searchBooks(query: String) {
        viewModelScope.launch {
            bookSearchUiState = BookSearchUiState.Loading
            bookSearchUiState = try {
                var bookList: List<Book> = bookRepository.listVolumes(query).items
                BookSearchUiState.Success(bookList)
            }
            catch (e: IOException) {
                BookSearchUiState.Error
            }
            catch(e: HttpException)
            {
                BookSearchUiState.Error
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