package com.example.petcare2.core.domain.repository

import com.example.petcare2.core.domain.model.Pet
import kotlinx.coroutines.flow.StateFlow

interface PetRepository {
    suspend fun getPets(): Result<List<Pet>>

    suspend fun getPetById(id: Int): Result<Pet>

    val favoriteIds: StateFlow<Set<Int>>

    fun toggleFavorite(id: Int)
}