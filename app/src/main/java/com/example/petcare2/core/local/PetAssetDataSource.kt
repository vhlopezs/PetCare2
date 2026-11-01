package com.example.petcare2.core.local

import android.content.Context
import com.example.petcare2.core.data.model.PetDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class PetAssetDataSource(private val context: Context) {
    suspend fun readPet(): List<PetDto> = withContext(Dispatchers.IO) {
        val json = context.assets.open("mascotas_practica_01.json")
            .bufferedReader()
            .use { it.readText() }
        Json.decodeFromString<List<PetDto>>(json)
    }
}