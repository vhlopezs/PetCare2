package com.example.petcare2.core.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.example.petcare2.R
import kotlinx.serialization.descriptors.SerialDescriptor

@Composable
fun PetCareAsyncImage(
    data: Any?,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(data)
            .crossfade(true)
            .error(R.drawable.ic_launcher_background)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}