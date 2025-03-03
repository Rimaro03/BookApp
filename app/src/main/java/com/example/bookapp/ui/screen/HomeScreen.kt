package com.example.bookapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookapp.models.Book
import com.example.bookapp.R
import com.example.bookapp.models.ImageLinks
import com.example.bookapp.models.SaleInfo
import com.example.bookapp.models.SearchInfo
import com.example.bookapp.models.VolumeInfo
import com.example.bookapp.ui.theme.BookAppTheme

@Composable
fun HomeScreen (
    volumeListUiState: VolumeListUiState,
    //search: (String) -> Unit,
    onBookClick: (String) -> Unit,
    retryAction: () -> Unit,
)
{
    when(volumeListUiState) {
        is VolumeListUiState.Loading -> LoadinScreen()
        is VolumeListUiState.Error -> ErrorScreen(retryAction = retryAction)
        is VolumeListUiState.Success -> {
            BookList(
                books = volumeListUiState.books,
                onBookClick = onBookClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookList(
    books: Map<String, List<Book>>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onBookClick: (String) -> Unit,
) {
    val query = remember { mutableStateOf("") }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
    ) {
        Column {
            Text(
                text = "Every book you want",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Right here",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                OutlinedTextField(
                    value = query.value,
                    onValueChange = {
                        query.value = it
                    },
                    enabled = true,
                    label = { Text("Search") },
                    singleLine = true,
                    modifier = Modifier
                        .padding(
                            top = 20.dp,
                            bottom = 20.dp
                        )
                        .weight(1f),
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    },
                )
                FilledIconButton (
                    onClick = {
                        //if(query.value.isNotEmpty()) search(query.value)
                    },
                    modifier = Modifier
                        .padding(
                            top = 6.dp
                        )
                        .size(55.dp),
                    shape = MaterialTheme.shapes.small,
                ){
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            }
        }

        Column {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.headlineSmall,
            )
            LazyRow {
                for((_, bookList) in books) {
                    items(
                        items = bookList,
                        key = { book -> book.id }
                    ) { book ->
                        AssistChip(
                            onClick = { /*TODO*/ },
                            label = { Text(book.volumeInfo.categories?.get(0) ?: "") },
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            }
        }

        for((category, bookList) in books) {
            Column {
                Text(
                    text = category,
                    style = MaterialTheme.typography.headlineSmall,
                )
                LazyRow {
                    items(
                        items = bookList,
                        key = { book -> book.id }
                    ) { book ->
                        BookCard(
                            book = book,
                            onBookClick = onBookClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    onBookClick: (String) -> Unit,
) {
    Card (
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .width(110.dp)
            .aspectRatio(.7f),
        onClick = {
            onBookClick(book.id)
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
}

@Preview
@Composable
fun BookCardPreview() {
    BookAppTheme {
        val book = Book(
            kind = "",
            id = "",
            etag = "",
            selfLink = "",
            volumeInfo = VolumeInfo(
                title = "The Great Gatsby",
                authors = listOf("F. Scott Fitzgerald"),
                publisher = "",
                publishedDate = "",
                description = "A story of decadence and decadence",
                industryIdentifiers = listOf(),
                pageCount = 0,
                categories = listOf(),
                imageLinks = ImageLinks(
                    smallThumbnail = "",
                    thumbnail = "",
                    small = "",
                    medium = "http://www.google.com",
                    large = "",
                ),
                language = "",
                previewLink = "",
                infoLink = "",
            ),
            saleInfo = SaleInfo(
                listPrice = null,
                buyLink = ""
            ),
            searchInfo = SearchInfo(
                textSnippet = "A story of decadence and decadence"
            )
        )
        BookCard(
            book = book,
            onBookClick = {}
        )
    }
}