package com.example.petcare2.detail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petcare2.core.composables.PetCareAsyncImage
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    petId: Int,
    viewModel: DetailViewModel = koinViewModel(
        key = "detail_$petId"
    ) {
        parametersOf(petId)
    },
    onBack: () -> Unit,

    ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()

    val isFavorite = (uiState.content as? DetailContent.Success)?.pet?.id in favoriteIds

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DETALLE") },
                navigationIcon = {
                    IconButton(onClick = onBack) { }
                },
                actions = {
                    IconButton(onClick = viewModel::onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            DetailScreenContent(
                uiState = uiState
            )
        }
    }
}

@Composable
fun DetailScreenContent(
    uiState: DetailUiState,
    modifier: Modifier = Modifier
) {
    when (val content = uiState.content) {
        DetailContent.Idle -> {}
        is DetailContent.Error -> {
            Text(text = "${content.msg}")
        }

        is DetailContent.Success -> {
            val pet = content.pet

            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PetCareAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    data = pet.imagen,
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = pet.nombre
                )
                Text(
                    text = "${pet.tipo}  * ${pet.raza} * ${pet.edad} ${if (pet.edad > 1) "años" else "año"} "
                )
                Text(
                    text = "Información"
                )
                RowPetDetail(
                    title = "Propietario",
                    value = pet.propietario
                )

                RowPetDetail(
                    title = "Fecha de nacimiento",
                    value = pet.proximaCita
                )

                RowPetDetail(
                    title = "Peso",
                    value = "${pet.peso} kg"
                )

                RowPetDetail(
                    title = "Sexo",
                    value = pet.sexo
                )

                RowPetDetail(
                    title = "Estado",
                    value = pet.estado
                )
            }
        }
    }
}

@Composable
fun RowPetDetail(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title
        )
        Text(
            text = value
        )
    }
}