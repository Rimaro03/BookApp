package com.example.bookapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookapp.R
import com.example.bookapp.models.Book

@Composable
fun BookSearchScreen(
    bookSearchUiState: BookSearchUiState,
    retryAction: () -> Unit,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    when(bookSearchUiState) {
        is BookSearchUiState.Loading -> LoadinScreen()
        is BookSearchUiState.Error -> ErrorScreen(retryAction)
        is BookSearchUiState.Success -> BookGrid(
            bookList = bookSearchUiState.books,
            onBookClick = onBookClick
        )
    }
}

@Composable
fun BookGrid(
    bookList: List<Book>,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn (
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
    ) {
        for (book in bookList) {
            item {
                BookSearchCard(
                    book = book,
                    onBookClick = onBookClick
                )
            }
        }
    }
}

@Composable
fun BookSearchCard (
    book: Book,
    modifier: Modifier = Modifier,
    onBookClick: (Book) -> Unit
) {
    Row (
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .width(100.dp),
            onClick = {
                onBookClick(book)
            }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(book.volumeInfo.imageLinks?.thumbnail?.replace("http", "https"))
                    .crossfade(true)
                    .build(),
                contentDescription = book.volumeInfo.title,
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
            )
        }
        Column (
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = book.volumeInfo.title ?: "",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = book.volumeInfo.authors?.get(0) ?: "",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
            )

            if(book.saleInfo.isEbook == true) {
                Text(
                    text = "Ebook",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                )
            }
            Text(
                text = "${book.saleInfo.listPrice?.currencyCode ?: ""} ${book.saleInfo.listPrice?.amount ?: ""}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
            )
        }
    }
}