package com.example.bookapp.data.local

import com.example.bookapp.models.Book
import com.example.bookapp.models.ImageLinks
import com.example.bookapp.models.IndustryIdentifier
import com.example.bookapp.models.ListPrice
import com.example.bookapp.models.SaleInfo
import com.example.bookapp.models.SearchInfo
import com.example.bookapp.models.VolumeInfo

object MockData {
    fun BookMockData(): Book {
        return Book(
            kind = "books#volume",
            id = "ubERDAAAQBAJ",
            etag = "BXqXCbTLTqQ",
            selfLink = "https://www.googleapis.com/books/v1/volumes/ubERDAAAQBAJ",
            volumeInfo = VolumeInfo(
                title = "The History of Jazz",
                authors = listOf("Ted Gioia"),
                publisher = "Oxford University Press, USA",
                publishedDate = "2011-05-09",
                description = "Ted Gioia's History of Jazz has been universally hailed as a classic--acclaimed by jazz critics and fans around the world. Now Gioia brings his magnificent work completely up-to-date, drawing on the latest research and revisiting virtually every aspect of the music, past and present...",
                industryIdentifiers = listOf(
                    IndustryIdentifier("ISBN_10", "0195399706"),
                    IndustryIdentifier("ISBN_13", "9780195399707")
                ),
                pageCount = 444,
                categories = listOf(
                    "Music / History & Criticism",
                    "Music / Genres & Styles / Jazz"
                ),
                imageLinks = ImageLinks(
                    smallThumbnail = "http://books.google.com/books/publisher/content?id=ubERDAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
                    thumbnail = "http://books.google.com/books/publisher/content?id=ubERDAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                    small = "http://books.google.com/books/publisher/content?id=ubERDAAAQBAJ&printsec=frontcover&img=1&zoom=2&edge=curl&source=gbs_api",
                    medium = "http://books.google.com/books/publisher/content?id=ubERDAAAQBAJ&printsec=frontcover&img=1&zoom=3&edge=curl&source=gbs_api",
                    large = "http://books.google.com/books/publisher/content?id=ubERDAAAQBAJ&printsec=frontcover&img=1&zoom=4&edge=curl&source=gbs_api"
                ),
                language = "en",
                previewLink = "http://books.google.it/books?id=ubERDAAAQBAJ&hl=&source=gbs_api",
                infoLink = "https://play.google.com/store/books/details?id=ubERDAAAQBAJ"
            ),
            saleInfo = SaleInfo(
                listPrice = ListPrice(
                    amount = 19.99,
                    currencyCode = "USD"
                ),
                buyLink = "http://books.google.it/books?id=ubERDAAAQBAJ&hl=&source=gbs_api"
            ),
            searchInfo = SearchInfo(
                textSnippet = "A panoramic history of the genre brings to life the diverse places in which jazz evolved, traces the origins of its various styles, and offers commentary on the music itself."
            )
        )
    }
}