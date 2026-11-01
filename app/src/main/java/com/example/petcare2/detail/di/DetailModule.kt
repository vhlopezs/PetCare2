package com.example.petcare2.detail.di

import com.example.petcare2.detail.domain.usecase.GetPetByIdUseCase
import com.example.petcare2.detail.ui.DetailViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    factoryOf(::GetPetByIdUseCase)

    viewModel { (petId: Int) ->
        DetailViewModel(
            petId = petId,
            getPetByIdUseCase = get(),
            toggleFavoriteUseCase = get(),
            favoritesRepository = get()
        )
    }
}