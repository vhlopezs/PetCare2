package com.example.petcare2.core.domain.usecase

import com.example.petcare2.core.domain.model.Pet
import com.example.petcare2.core.domain.repository.PetRepository

class GetPetsUseCase(private val repository: PetRepository) {
    suspend operator fun invoke() = repository.getPets()
}