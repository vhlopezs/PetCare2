package com.example.petcare2.core.di

import com.example.petcare2.core.data.repository.PetRepositoryImpl
import com.example.petcare2.core.domain.repository.PetRepository
import com.example.petcare2.core.domain.usecase.GetPetsUseCase
import com.example.petcare2.core.domain.usecase.ToggleFavoriteUseCase
import com.example.petcare2.core.local.PetAssetDataSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::PetAssetDataSource)
    singleOf(::PetRepositoryImpl) { bind<PetRepository>() }

    factoryOf(::GetPetsUseCase)
    factoryOf(::ToggleFavoriteUseCase)
}