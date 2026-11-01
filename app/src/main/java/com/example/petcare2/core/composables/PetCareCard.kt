package com.example.petcare2.core.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.petcare2.core.domain.model.Pet

@Composable
fun PetCareCard(
    pet: Pet,
    onItemSelected: (Int) -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(130.dp),
        onClick = { onItemSelected(pet.id) }
    ) {
        Row(
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PetCareAsyncImage(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                data = pet.imagen,
                contentScale = ContentScale.Crop
            )

            PetInformationContent(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                nombre = pet.nombre,
                tipo = pet.tipo,
                raza = pet.raza,
                estado = pet.estado,
                edad = pet.edad
            )

            Column(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onFavoriteClick(pet.id) },
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun PetInformationContent(
    nombre: String,
    raza: String,
    edad: Int,
    estado: String,
    tipo: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = nombre
        )
        Text(
            text = "$tipo * $raza"
        )
        Text(text = "$edad * $estado")
    }
}