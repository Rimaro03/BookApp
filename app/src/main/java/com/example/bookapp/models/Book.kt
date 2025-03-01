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
    val saleInfo: SaleInfo
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class VolumeInfo (
    val title: String,
    val authors: List<String>,
    val publisher: String? = "",
    val publishedDate: String,
    val description: String? = "",
    val industryIdentifiers: List<IndustryIdentifier>,
    val pageCount: Int,
    val categories: List<String>? = null,
    val imageLinks: ImageLinks,
    val language: String,
    val previewLink: String,
    val infoLink: String,
)

@Serializable
data class IndustryIdentifier (
    val type: String,
    val identifier: String
)

@Serializable
data class ImageLinks (
    val smallThumbnail: String,
    val thumbnail: String
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class SaleInfo (
    val listPrice: ListPrice? = null,
    val buyLink: String? = null
)

@Serializable
data class ListPrice (
    val amount: Double,
    val currencyCode: String
)