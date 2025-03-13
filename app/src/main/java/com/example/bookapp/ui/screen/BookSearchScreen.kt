package com.example.bookapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookapp.R
import com.example.bookapp.models.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSearchScreenAppBar(
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
fun BookSearchScreen(
    bookSearchUiState: BookSearchUiState,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit = {},
    onSearch: (String) -> Unit,
    retryAction: () -> Unit,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold (
        modifier = Modifier,
        topBar = {
            BookSearchScreenAppBar(
                canNavigateBack = canNavigateBack,
                navigateBack = navigateBack,
                onSearch = onSearch,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        when(bookSearchUiState) {
            is BookSearchUiState.Loading -> LoadinScreen()
            is BookSearchUiState.Error -> ErrorScreen(retryAction)
            is BookSearchUiState.Success -> BookGrid(
                bookList = bookSearchUiState.books,
                onBookClick = onBookClick,
                modifier = Modifier.padding(innerPadding)
            )
        }
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
            .fillMaxWidth()
            .clickable {
                onBookClick(book)
            },
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .width(100.dp)
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