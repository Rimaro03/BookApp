package com.example.bookapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import com.example.bookapp.models.Book

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BookScreenAppBar(
    canNavigateBack: Boolean,
    navigateBack: () -> Unit = {},
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
){
    val textFieldState = rememberTextFieldState()
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box (
        modifier = modifier
            .fillMaxWidth()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = modifier.align(Alignment.TopCenter),
            inputField = {
                SearchBarDefaults.InputField(
                    state = textFieldState,
                    onSearch = {
                        expanded = false
                        onSearch(textFieldState.text.toString())
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Hinted search text") },
                    trailingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    leadingIcon = {
                        if (canNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable { navigateBack() }
                            )
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            Column(
                Modifier.verticalScroll(rememberScrollState())
            ) {
                for (category in listOf("Fiction", "History", "Lotr")){
                    ListItem(
                        headlineContent = { Text(category) },
                        leadingContent = {
                            Icon(
                                Icons.Filled.History,
                                contentDescription = null
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                textFieldState.setTextAndPlaceCursorAtEnd(category)
                                expanded = false
                                onSearch(category)
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun BookScreen (
    bookUiState: BookDetailUiState,
    retryAction: () -> Unit,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit = {},
    onSearch: (String) -> Unit,
    sampleButtonAction: (String) -> Unit,
    buyButtonAction: (String) -> Unit
) {
    Scaffold (
        modifier = Modifier,
        topBar = {
            BookScreenAppBar(
                canNavigateBack = canNavigateBack,
                navigateBack = navigateBack,
                onSearch = onSearch,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        when (bookUiState) {
            is BookDetailUiState.Loading -> LoadinScreen()
            is BookDetailUiState.Error -> ErrorScreen(retryAction = retryAction)
            is BookDetailUiState.Success -> BookDetail(
                book = bookUiState.book,
                bookDetail = bookUiState.bookDetail,
                sampleButtonAction = sampleButtonAction,
                buyButtonAction = buyButtonAction,
                modifier = Modifier.padding(innerPadding),
                onSearch = onSearch
            )
        }
    }
}

@Composable
fun BookDetail(
    book: Book,
    bookDetail: Book,
    modifier: Modifier = Modifier,
    sampleButtonAction: (String) -> Unit,
    buyButtonAction: (String) -> Unit,
    onSearch: (String) -> Unit
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
                onClick = {
                    sampleButtonAction(book.volumeInfo.previewLink ?: "https://www.google.com")
                },
                modifier = Modifier
                    .weight(1f)
            ) {
                Text("Sample")
            }

            Button (
                onClick = {
                    buyButtonAction(book.saleInfo.buyLink ?: "https://www.google.com")
                },
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
                    modifier = Modifier
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
                        onClick = {
                            onSearch(category)
                        },
                        label = {
                            Text(
                               text = category,
                                modifier = Modifier.padding(5.dp)
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