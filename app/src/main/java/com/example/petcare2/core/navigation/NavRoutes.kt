package com.example.petcare2.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface NavRoutes : NavKey {
    @Serializable
    data object Home : NavRoutes

    @Serializable
    data object Favorite : NavRoutes

    @Serializable
    data class Detail(val petId: Int) : NavRoutes


}