package com.example.petcare2.favorite.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petcare2.core.composables.PetCareCard
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = koinViewModel(),
    onBack: () -> Unit,
    navigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FAVORITOS") },
                navigationIcon = {
                    IconButton(onClick = onBack) { }
                }
            )
        }
    ) { paddingValues ->
        FavoriteScreenContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            favoriteIds = favoriteIds,
            onToggleFavorite = viewModel::onToggleFavorite,
            onItemSelected = navigateToDetail
        )
    }
}

@Composable
fun FavoriteScreenContent(
    uiState: FavoriteUiState,
    favoriteIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (val content = uiState.content) {
        FavoriteContent.Empty -> {}
        FavoriteContent.Idle -> {}
        is FavoriteContent.Success -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(content.pets, key = { it.id }) { pet ->
                    PetCareCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        pet = pet,
                        isFavorite = pet.id in favoriteIds,
                        onFavoriteClick = { onToggleFavorite(pet.id) },
                        onItemSelected = onItemSelected
                    )
                }
            }
        }
    }
}