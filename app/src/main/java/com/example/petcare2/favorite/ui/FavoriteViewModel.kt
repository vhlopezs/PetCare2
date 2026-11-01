package com.example.petcare2.favorite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare2.core.domain.model.Pet
import com.example.petcare2.core.domain.repository.PetRepository
import com.example.petcare2.core.domain.usecase.GetPetsUseCase
import com.example.petcare2.core.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoriteUiState(
    val isLoading: Boolean = true,
    val content: FavoriteContent = FavoriteContent.Idle
)

sealed interface FavoriteContent {
    data object Idle : FavoriteContent
    data object Empty : FavoriteContent
    data class Success(val pets: List<Pet>) : FavoriteContent
}

class FavoriteViewModel(
    private val getPetsUseCase: GetPetsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val petRepository: PetRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    val favoriteIds: StateFlow<Set<Int>> = petRepository.favoriteIds

    private var allPets: List<Pet> = emptyList()

    init {
        loadPets()
        observeFavorites()
    }

    private fun observeFavorites() {
        petRepository.favoriteIds
            .onEach { applyFilter() }
            .launchIn(viewModelScope)
    }

    fun onToggleFavorite(id: Int) = toggleFavoriteUseCase(id)

    private fun loadPets() = viewModelScope.launch {
        getPetsUseCase()
            .onSuccess { pets ->
                allPets = pets
                applyFilter()
            }
            .onFailure {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        content = FavoriteContent.Empty
                    )
                }
            }
    }

    private fun applyFilter() {
        val favorites = petRepository.favoriteIds.value
        val favoritesFiltered = allPets.filter { it.id in favorites }

        _uiState.update {
            it.copy(
                isLoading = false,
                content = if (favoritesFiltered.isEmpty()) FavoriteContent.Empty
                else FavoriteContent.Success(
                    favoritesFiltered
                )
            )
        }
    }

}