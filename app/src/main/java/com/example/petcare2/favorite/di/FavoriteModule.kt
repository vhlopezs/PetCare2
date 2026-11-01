package com.example.petcare2.favorite.di

import com.example.petcare2.favorite.ui.FavoriteViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val favoriteModule = module {
    viewModelOf(::FavoriteViewModel)
}