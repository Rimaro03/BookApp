package com.example.bookapp.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookapp.ui.screen.BookScreen
import com.example.bookapp.ui.screen.BookSearchScreen
import com.example.bookapp.ui.screen.BookViewModel
import com.example.bookapp.ui.screen.HomeScreen

enum class AppScreen() {
    HomeScreen(),
    BookScreen(),
    BookSearch()
}

fun openBrowser(context: Context, link: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    context.startActivity(browserIntent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookApp() {
    val bookViewModel: BookViewModel = viewModel(factory = BookViewModel.factory)
    val volumeListUiState = bookViewModel.volumeListUiState
    val bookDetailUiState = bookViewModel.bookDetailUiState
    val bookSearchUiState = bookViewModel.bookSearchUiState

    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStack?.destination?.route ?: AppScreen.HomeScreen.name
    )

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = AppScreen.HomeScreen.name,
        modifier = Modifier
    ) {
        composable(AppScreen.HomeScreen.name) {
            HomeScreen(
                volumeListUiState = volumeListUiState,
                onSearch = { query ->
                    bookViewModel.searchBooks(query)
                    navController.navigate(AppScreen.BookSearch.name)
                },
                onBookClick = { book ->
                    bookViewModel.getBook(book)
                    navController.navigate(AppScreen.BookScreen.name)
                },
                retryAction = bookViewModel::getVolumeList
            )
        }
        composable(AppScreen.BookScreen.name) {
            BookScreen(
                bookUiState = bookDetailUiState,
                retryAction = bookViewModel::getVolumeList,
                sampleButtonAction = { link ->
                    openBrowser(context, link = link)
                },
                buyButtonAction = { link ->
                    openBrowser(context, link)
                }
            )
        }
        composable(AppScreen.BookSearch.name) {
            BookSearchScreen(
                bookSearchUiState = bookSearchUiState,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateBack = { navController.navigateUp() },
                onSearch = { query ->
                    bookViewModel.searchBooks(query)
                    navController.navigate(AppScreen.BookSearch.name)
                },
                retryAction = bookViewModel::getVolumeList,
                onBookClick = { book ->
                    bookViewModel.getBook(book)
                    navController.navigate(AppScreen.BookScreen.name)
                }
            )
        }
    }

}