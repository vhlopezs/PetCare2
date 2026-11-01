package com.example.petcare2.home.di

import com.example.petcare2.core.domain.usecase.ToggleFavoriteUseCase
import com.example.petcare2.home.ui.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
}