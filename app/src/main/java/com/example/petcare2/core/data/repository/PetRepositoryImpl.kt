package com.example.petcare2.core.data.repository

import com.example.petcare2.core.data.mapper.toDomain
import com.example.petcare2.core.domain.model.Pet
import com.example.petcare2.core.domain.repository.PetRepository
import com.example.petcare2.core.local.PetAssetDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PetRepositoryImpl(
    private val dataSource: PetAssetDataSource
) : PetRepository {
    private var cache: List<Pet>? = null

    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())

    override val favoriteIds: StateFlow<Set<Int>> = _favoriteIds.asStateFlow()

    override suspend fun getPets(): Result<List<Pet>> = runCatching {
        cache ?: dataSource.readPet().map { it.toDomain() }.also { cache = it }
    }

    override suspend fun getPetById(id: Int): Result<Pet> = runCatching {
        getPets().getOrThrow().first { it.id == id }
    }


    override fun toggleFavorite(id: Int) {
        _favoriteIds.update { actuales ->
            if (id in actuales) actuales - id else actuales + id
        }
    }

}