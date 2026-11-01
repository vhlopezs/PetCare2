package com.example.petcare2.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare2.core.domain.model.Pet
import com.example.petcare2.core.domain.repository.PetRepository
import com.example.petcare2.core.domain.usecase.GetPetsUseCase
import com.example.petcare2.core.domain.usecase.ToggleFavoriteUseCase
import com.example.petcare2.home.domain.model.PetCategory
import com.example.petcare2.home.domain.model.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val totalPets: Int = 0,
    val totalDogs: Int = 0,
    val totalCats: Int = 0,
    val query: String = "",
    val petCategorySelected: String = PetCategory.TODOS.label,
    val sortOption: SortOption = SortOption.NAME,
    val content: HomeContent = HomeContent.Idle
)

sealed interface HomeContent {
    data object Idle : HomeContent
    data object Empty : HomeContent
    data class Success(val pets: List<Pet>) : HomeContent
    data class Error(val msg: String) : HomeContent
}

class HomeViewModel(
    private val getPetsUseCase: GetPetsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val petRepository: PetRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val favoriteIds = petRepository.favoriteIds

    private var allPets: List<Pet> = emptyList()

    init {
        loadPets()
        obserFavorites()
    }

    fun onQueryChanged(value: String) {
        _uiState.update { it.copy(query = value) }
        applyFilters()
    }

    fun onCategoryChanged(value: String) {
        _uiState.update { it.copy(petCategorySelected = value) }
        applyFilters()
    }

    fun onSortOptionChanged(value: SortOption) {
        _uiState.update { it.copy(sortOption = value) }
        applyFilters()
    }

    fun onToggleFavorite(id: Int) = toggleFavoriteUseCase(id)

    private fun obserFavorites() {
        petRepository.favoriteIds
            .onEach { applyFilters() }
            .launchIn(viewModelScope)
    }

    private fun loadPets() = viewModelScope.launch {
        getPetsUseCase()
            .onSuccess { pets ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        totalPets = pets.size,
                        totalDogs = pets.count { dog -> dog.tipo == PetCategory.PERRO.label },
                        totalCats = pets.count { cat -> cat.tipo == PetCategory.GATO.label },
                    )
                }
                allPets = pets
                applyFilters()
                Log.d("HomeViewModel-LOG", "loadPets:  ${_uiState.value}")
            }
            .onFailure { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        content = HomeContent.Error(e.message ?: "Unknow Error")
                    )
                }
            }
    }

    private fun applyFilters() {
        val state = _uiState.value

        val filtered = allPets.filter { pet ->
            val matchesQuery = pet.nombre.contains(state.query, ignoreCase = true)
            val matchesCategory =
                state.petCategorySelected == PetCategory.TODOS.label || pet.tipo == state.petCategorySelected

            matchesQuery && matchesCategory
        }

        val sorted = when (state.sortOption) {
            SortOption.NAME -> filtered.sortedBy { it.nombre }
            SortOption.DATE -> filtered.sortedBy { it.proximaCita }
        }

        _uiState.update {
            it.copy(
                isLoading = false,
                content =
                    if (sorted.isEmpty()) HomeContent.Empty else HomeContent.Success(sorted)
            )
        }
    }
}