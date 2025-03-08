package com.example.bookapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.bookapp.data.local.MockData
import com.example.bookapp.ui.AppScreen
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import com.example.bookapp.ui.theme.BookAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenAppBar(
    modifier: Modifier = Modifier,
    currentScreens: AppScreen,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit = {},
){
    val textFieldState = rememberTextFieldState()
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box (
        modifier = Modifier.fillMaxWidth().semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            inputField = {
                SearchBarDefaults.InputField(
                    state = textFieldState,
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Hinted search text") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            Column(
                Modifier.verticalScroll(rememberScrollState())
            ) {
                repeat(4) { idx ->
                    val resultText = "Suggestion $idx"
                    ListItem(
                        headlineContent = { Text(resultText) },
                        supportingContent = { Text("Additional info") },
                        leadingContent = {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier =
                        Modifier
                            .clickable {
                                textFieldState.setTextAndPlaceCursorAtEnd(resultText)
                                expanded = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen (
    volumeListUiState: VolumeListUiState,
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit = {},
    search: (String) -> Unit,
    onBookClick: (Book) -> Unit,
    retryAction: () -> Unit,
)
{
    Scaffold (
        modifier = Modifier,
        topBar = {
            HomeScreenAppBar(
                currentScreens = currentScreen,
                canNavigateBack = canNavigateBack,
                navigateBack = navigateBack,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        when (volumeListUiState) {
            is VolumeListUiState.Loading -> LoadinScreen()
            is VolumeListUiState.Error -> ErrorScreen(retryAction = retryAction)
            is VolumeListUiState.Success -> {
                BookList(
                    books = volumeListUiState.books,
                    onBookClick = onBookClick,
                    search = search,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun BookList(
    books: Map<String, List<Book>>,
    modifier: Modifier = Modifier,
    onBookClick: (Book) -> Unit,
    search: (String) -> Unit
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
    onBookClick: (Book) -> Unit,
) {
    Card (
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .width(110.dp)
            .aspectRatio(.7f),
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
}

@Preview
@Composable
fun BookCardPreview() {
    BookAppTheme {
        BookCard(
            book = MockData.BookMockData(),
            onBookClick = {}
        )
    }
}