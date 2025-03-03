package com.example.bookapp.data

import com.example.bookapp.models.Book
import com.example.bookapp.models.BookListResponse
import com.example.bookapp.network.BookApiService

interface BookRepository {
    suspend fun listVolumes(query: String, apiKey: String): BookListResponse
    suspend fun getVolume(bookId: String, apiKey: String): Book
}

class NetworkBookRepository(
    private val bookApiService: BookApiService
): BookRepository {
    override suspend fun listVolumes(query: String, apiKey: String): BookListResponse =
        bookApiService.listVolumes(query, apiKey)
    override suspend fun getVolume(bookId: String, apiKey: String): Book =
        bookApiService.getVolume(bookId, apiKey)
}