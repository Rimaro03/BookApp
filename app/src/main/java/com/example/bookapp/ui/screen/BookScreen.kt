package com.example.bookapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedAssistChip
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
fun BookScreen (
    bookUiState: BookDetailUiState,
    retryAction: () -> Unit
) {
    when (bookUiState) {
        is BookDetailUiState.Loading -> LoadinScreen()
        is BookDetailUiState.Error -> ErrorScreen(retryAction =  retryAction)
        is BookDetailUiState.Success -> BookDetail(book = bookUiState.book)
    }
}

@Composable
fun BookDetail(
    book: Book,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // book image
        Card (
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .aspectRatio(.7f),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(book.volumeInfo.imageLinks?.large?.replace("http", "https"))
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
        // title and authors
        Column {
            Text(
                text = book.volumeInfo.title ?: "",
                style = MaterialTheme.typography.titleLarge,
            )
            Row {
                if(book.volumeInfo.authors != null){
                    for (author in book.volumeInfo.authors) {
                        Text(
                            text = if(author == book.volumeInfo.authors.last()) author else "$author, ",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                        )
                    }
                }
            }
        }

        // categories
        if(book.volumeInfo.categories != null){
            LazyRow {
                items(
                    items = book.volumeInfo.categories,
                    key = { category -> category }
                ) { category ->
                    ElevatedAssistChip(
                        onClick = {  },
                        label = {
                            Text(
                               text = category,
                                modifier = modifier.padding(10.dp)
                            )},
                        modifier = Modifier
                            .padding(
                                end = 8.dp
                            )
                    )
                }
            }
        }

        // description
        if(book.volumeInfo.description != null){
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = book.volumeInfo.description,
                )
            }
        }
    }
}

