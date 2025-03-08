package com.example.bookapp.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
data class BookListResponse (
    val kind: String,
    val totalItems: Int,
    val items: List<Book>
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class Book (
    val kind: String,
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo,
    val searchInfo: SearchInfo? = null
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class VolumeInfo (
    val title: String? = "",
    val subtitle: String? = "",
    val authors: List<String>? = listOf(),
    val publisher: String? = "",
    val publishedDate: String? = "",
    val description: String? = "",
    val industryIdentifiers: List<IndustryIdentifier>? = listOf(),
    val pageCount: Int? = 0,
    val averageRating: Int? = 0,
    val ratingsCount: Int? = 0,
    val categories: List<String>? = null,
    val imageLinks: ImageLinks? = null,
    val language: String? = "",
    val previewLink: String? = "",
    val infoLink: String? = "",
)

@Serializable
data class IndustryIdentifier (
    val type: String,
    val identifier: String
)

@Serializable
data class ImageLinks (
    val smallThumbnail: String,
    val thumbnail: String,
    val small: String? = null,
    val medium: String? = null,
    val large: String? = null,
    val extraLarge: String? = null
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class SaleInfo (
    val listPrice: ListPrice? = null,
    val buyLink: String? = null,
    val isEbook: Boolean? = false
)

@Serializable
data class ListPrice (
    val amount: Double,
    val currencyCode: String
)

@Serializable
data class SearchInfo (
    val textSnippet: String? = ""
)