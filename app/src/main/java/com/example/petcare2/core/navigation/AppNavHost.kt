package com.example.petcare2.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.petcare2.detail.ui.DetailScreen
import com.example.petcare2.favorite.ui.FavoriteScreen
import com.example.petcare2.home.ui.HomeScreen

@Composable
fun AppNavHost() {
    val backStack = rememberNavBackStack(NavRoutes.Home)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.Home> {
                HomeScreen(
                    navigateToDetail = { petId ->
                        backStack.add(NavRoutes.Detail(petId))
                    },
                    navigateToFavorite = { backStack.add(NavRoutes.Favorite) }
                )
            }

            entry<NavRoutes.Detail> { key ->
                DetailScreen(
                    petId = key.petId,
                    onBack = { backStack.removeLastOrNull() })
            }

            entry<NavRoutes.Favorite> {
                FavoriteScreen(
                    onBack = { backStack.removeLastOrNull() },
                    navigateToDetail = { petId ->
                        backStack.add(NavRoutes.Detail(petId))
                    }
                )
            }
        }
    )
}