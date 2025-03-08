package com.example.bookapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import com.example.bookapp.models.Book

@Composable
fun BookScreen (
    bookUiState: BookDetailUiState,
    retryAction: () -> Unit
) {
    when (bookUiState) {
        is BookDetailUiState.Loading -> LoadinScreen()
        is BookDetailUiState.Error -> ErrorScreen(retryAction =  retryAction)
        is BookDetailUiState.Success -> BookDetail(
            book = bookUiState.book,
            bookDetail = bookUiState.bookDetail
        )
    }
}

@Composable
fun BookDetail(
    book: Book,
    bookDetail: Book,
    modifier: Modifier = Modifier
) {
    var descriptionExpanded by remember { mutableStateOf(false) }
    val MAX_DESCRIPTION_LINES = 5

    Column (
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp),
    ) {
        // book image, title, authors, publisher
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Card(
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .width(120.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(bookDetail.volumeInfo.imageLinks?.small?.replace("http", "https"))
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
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = book.volumeInfo.title ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = book.volumeInfo.subtitle ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
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
                Text(
                    text = book.volumeInfo.publisher ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                )
            }
        }

        // infos
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text(
                        text = book.volumeInfo.averageRating?.toString() ?: "0",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Ratings"
                    )
                }
                Text(
                    text = "${book.volumeInfo.ratingsCount?.toString() ?: "0"} reviews",
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            // divider vertical
            VerticalDivider(
                modifier = Modifier
                    .height(30.dp)
            )
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.book_icon),
                    contentDescription = "Book Icon"
                )
                Text(
                    text = "Ebook",
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            // divider vertical
            VerticalDivider(
                modifier = Modifier
                    .height(30.dp)
            )
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = book.volumeInfo.pageCount.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Pages",
                    style = MaterialTheme.typography.titleSmall,
                )
            }

        }

        // sample and buy
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton (
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
            ) {
                Text("Sample")
            }

            Button (
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
            ) {
                Text("Buy ${bookDetail.saleInfo.listPrice?.currencyCode} ${bookDetail.saleInfo.listPrice?.amount}")
            }
        }

        // description
        if(book.volumeInfo.description != null){
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "About this book",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = book.volumeInfo.description,
                    maxLines = if (descriptionExpanded) Int.MAX_VALUE else MAX_DESCRIPTION_LINES,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier
                        .clickable {
                            descriptionExpanded = !descriptionExpanded
                        }
                )
            }
        }

        // categories
        if(bookDetail.volumeInfo.categories != null){
            LazyRow {
                items(
                    items = bookDetail.volumeInfo.categories,
                    key = { category -> category }
                ) { category ->
                    AssistChip (
                        onClick = {  },
                        label = {
                            Text(
                               text = category,
                                modifier = modifier.padding(5.dp)
                            )},
                        modifier = Modifier
                            .padding(
                                end = 8.dp
                            )
                    )
                }
            }
        }
    }
}