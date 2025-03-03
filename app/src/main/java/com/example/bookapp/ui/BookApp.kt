package com.example.bookapp.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.bookapp.ui.screen.BookScreen
import com.example.bookapp.ui.screen.HomeScreen

enum class AppScreens() {
    HomeScreen(),
    BookScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookApp() {
    Scaffold (
        modifier = Modifier
    ) { innerPadding ->
        val bookViewModel: BookViewModel = viewModel(factory = BookViewModel.factory)
        val volumeListUiState = bookViewModel.volumeListUiState
        val bookDetailUiState = bookViewModel.bookDetailUiState

        val navController = rememberNavController()
        val backStack by navController.currentBackStackEntryAsState()
        val currentScreen = AppScreens.valueOf(
            backStack?.destination?.route ?: AppScreens.HomeScreen.name
        )
        NavHost(
            navController = navController,
            startDestination = AppScreens.HomeScreen.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreens.HomeScreen.name) {
                HomeScreen(
                    volumeListUiState = volumeListUiState,
                    /*search = {query ->
                        bookViewModel.getVolumeList(query)
                    }*/
                )
            }
            composable(AppScreens.BookScreen.name) {
                BookScreen(
                    bookUiState = bookDetailUiState
                )
            }
        }
    }
}