package com.example.petcare2.core.domain.usecase

import com.example.petcare2.core.domain.repository.PetRepository

class ToggleFavoriteUseCase(private val repository: PetRepository) {
    operator fun invoke(id: Int) = repository.toggleFavorite(id)
}