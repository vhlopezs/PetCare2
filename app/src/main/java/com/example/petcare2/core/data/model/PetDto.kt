package com.example.petcare2.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PetDto(
    val id: Int,
    val nombre: String,
    val tipo: String,
    val raza: String,
    val edad: Int,
    val sexo: String,
    val peso: Double,
    val propietario: String,
    val estado: String,
    val proximaCita: String,
    val imagen: String
)