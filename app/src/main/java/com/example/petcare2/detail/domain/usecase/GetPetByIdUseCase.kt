package com.example.petcare2.detail.domain.usecase

import com.example.petcare2.core.domain.model.Pet
import com.example.petcare2.core.domain.repository.PetRepository

class GetPetByIdUseCase(private val petRepository: PetRepository) {
    suspend operator fun invoke(id: Int): Result<Pet> = petRepository.getPetById(id)
}