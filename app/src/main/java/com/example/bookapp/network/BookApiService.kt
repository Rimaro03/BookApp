package com.example.bookapp.network

import com.example.bookapp.models.Book
import com.example.bookapp.models.BookListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {
    @GET("volumes")
    suspend fun listVolumes(@Query("q") query: String): BookListResponse

    @GET("volumes/{id}")
    suspend fun getVolume(@Path("id") bookId: String): Book
}