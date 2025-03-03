package com.example.bookapp.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookapp.ui.screen.BookViewModel
import com.example.bookapp.ui.screen.VolumeListUiState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookapp.ui.screen.BookDetailUiState
import com.example.bookapp.ui.screen.BookScreen
import com.example.bookapp.ui.screen.HomeScreen

enum class AppScreen() {
    HomeScreen(),
    BookScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppBar(
    modifier: Modifier = Modifier,
    currentScreens: AppScreen,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit = {},
){
    TopAppBar(
        title = {
            if(currentScreens == AppScreen.HomeScreen) {
                Text("BookApp")
            }
            else {
                Text("Discover your book")
            }
        },
        modifier = modifier,
        navigationIcon = {
            if(canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Account"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookApp() {
    val bookViewModel: BookViewModel = viewModel(factory = BookViewModel.factory)
    val volumeListUiState = bookViewModel.volumeListUiState
    val bookDetailUiState = bookViewModel.bookDetailUiState

    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStack?.destination?.route ?: AppScreen.HomeScreen.name
    )

    Scaffold (
        modifier = Modifier,
        topBar = {
            BookAppBar(
                currentScreens = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateBack = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.HomeScreen.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.HomeScreen.name) {
                HomeScreen(
                    volumeListUiState = volumeListUiState,
                    /*search = {query ->
                        bookViewModel.getVolumeList(query)
                    }*/
                    onBookClick = { bookId ->
                        bookViewModel.getBook(bookId)
                        navController.navigate(AppScreen.BookScreen.name)
                    },
                    retryAction = bookViewModel::getVolumeList
                )
            }
            composable(AppScreen.BookScreen.name) {
                BookScreen(
                    bookUiState = bookDetailUiState,
                    retryAction = bookViewModel::getVolumeList
                )
            }
        }
    }
}