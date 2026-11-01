package com.example.petcare2.detail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare2.core.data.repository.PetRepositoryImpl
import com.example.petcare2.core.domain.model.Pet
import com.example.petcare2.core.domain.usecase.ToggleFavoriteUseCase
import com.example.petcare2.detail.domain.usecase.GetPetByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val isLoading: Boolean = true,
    val content: DetailContent = DetailContent.Idle
)

sealed interface DetailContent {
    data object Idle : DetailContent
    data class Success(val pet: Pet) : DetailContent
    data class Error(val msg: String) : DetailContent
}

class DetailViewModel(
    private val petId: Int,
    private val getPetByIdUseCase: GetPetByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    favoritesRepository: PetRepositoryImpl
) : ViewModel() {

    private val _uiSate = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiSate.asStateFlow()

    val favoriteIds: StateFlow<Set<Int>> = favoritesRepository.favoriteIds

    init {
        loadPet()
    }

    fun onToggleFavorite() = toggleFavoriteUseCase(petId)

    private fun loadPet() = viewModelScope.launch {
        getPetByIdUseCase(petId)
            .onSuccess { pet ->
                _uiSate.update {
                    it.copy(
                        isLoading = false,
                        content = DetailContent.Success(pet)
                    )
                }
            }
            .onFailure { e ->
                _uiSate.update {
                    it.copy(
                        isLoading = false,
                        content = DetailContent.Error(e.message ?: "Unknown Error")
                    )
                }
            }
    }
}