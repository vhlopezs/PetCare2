package com.example.petcare2.home.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petcare2.R
import com.example.petcare2.core.composables.PetCareAsyncImage
import com.example.petcare2.core.composables.PetCareCard
import com.example.petcare2.home.domain.model.PetCategory
import com.example.petcare2.home.domain.model.SortOption
import com.example.petcare2.home.ui.composables.PetCareIndicatorBox
import com.example.petcare2.home.ui.composables.PetCareOutlinedTextField
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigateToDetail: (Int) -> Unit,
    navigateToFavorite: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            HomeSCreenContent(
                uiState = uiState,
                onItemSelected = navigateToDetail,
                favoriteIds = favoriteIds,
                onFavoriteClick = viewModel::onToggleFavorite,
                navigateToFavorite = navigateToFavorite,
                onQueryChanged = viewModel::onQueryChanged,
                onCategoryChanged = viewModel::onCategoryChanged,
                selected = uiState.sortOption,
                onSortOptionSelected = viewModel::onSortOptionChanged
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun HomeSCreenContent(
    uiState: HomeUiState,
    onItemSelected: (Int) -> Unit,
    favoriteIds: Set<Int>,
    onFavoriteClick: (Int) -> Unit,
    navigateToFavorite: () -> Unit,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onCategoryChanged: (String) -> Unit,
    selected: SortOption,
    onSortOptionSelected: (SortOption) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            HeaderContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                navigateToFavorite = navigateToFavorite
            )
        }

        item {
            IndicatorContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                totalPets = uiState.totalPets,
                totalDogs = uiState.totalDogs,
                totalCats = uiState.totalCats
            )
        }

        item {
            SearchContent(
                modifier = Modifier
                    .fillMaxWidth(),
                query = uiState.query,
                onQueryChanged = onQueryChanged,
                onCategoryChanged = onCategoryChanged,
                selected = selected,
                onSortOptionSelected = onSortOptionSelected,
                categorySelected = uiState.petCategorySelected
            )
        }

        when (val content = uiState.content) {
            is HomeContent.Success -> {
                items(content.pets, key = { it.id }) { pet ->
                    Log.d("HomeSreen-LOG", "HomeContent: ${pet.imagen}")
                    PetCareCard(
                        pet = pet,
                        onItemSelected = onItemSelected,
                        isFavorite = pet.id in favoriteIds,
                        onFavoriteClick = onFavoriteClick,
                        modifier = modifier
                    )
                }
            }

            is HomeContent.Empty -> {
                item { Text("No se encotraron mascotas") }
            }

            is HomeContent.Error -> Log.d("HomeScreen-LOG", "Error: ${content.msg}")
            else -> Unit


        }
    }
}

@Composable
fun HeaderContent(
    navigateToFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "PetCare",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(50.dp))
        IconButton(
            onClick = navigateToFavorite,
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null
            )
        }

        Text(
            text = "Favoritos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SearchContent(
    query: String,
    selected: SortOption,
    onSortOptionSelected: (SortOption) -> Unit,
    onQueryChanged: (String) -> Unit,
    categorySelected: String,
    onCategoryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        PetCareOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            placeholder = "Buscar mascota",
            onValueChanged = onQueryChanged
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            PetCategory.entries.forEach { category ->
                FilterChip(
                    selected = categorySelected == category.label,
                    onClick = { onCategoryChanged(category.label) },
                    label = { Text(text = category.label) }
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SortOption.entries.forEach { sort ->
                FilterChip(
                    selected = selected == sort,
                    onClick = { onSortOptionSelected(sort) },
                    label = { Text(text = sort.label) }
                )
            }
        }
    }
}

@Composable
fun IndicatorContent(
    totalPets: Int,
    totalDogs: Int,
    totalCats: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PetCareIndicatorBox(
            modifier = Modifier.weight(1f),
            title = "Total",
            value = totalPets,
            color = MaterialTheme.colorScheme.primary
        )

        PetCareIndicatorBox(
            modifier = Modifier.weight(1f),
            title = "Perros",
            value = totalDogs,
            color = MaterialTheme.colorScheme.primary
        )

        PetCareIndicatorBox(
            modifier = Modifier.weight(1f),
            title = "Gatos",
            value = totalCats,
            color = MaterialTheme.colorScheme.primary
        )
    }
}