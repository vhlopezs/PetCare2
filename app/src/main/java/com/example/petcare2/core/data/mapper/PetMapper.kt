package com.example.petcare2.core.data.mapper

import com.example.petcare2.core.data.model.PetDto
import com.example.petcare2.core.domain.model.Pet

fun PetDto.toDomain() = Pet(
    id = id,
    nombre = nombre,
    tipo = tipo,
    raza = raza,
    edad = edad,
    estado = estado,
    sexo = sexo,
    peso = peso,
    propietario = propietario,
    proximaCita = proximaCita,
    imagen = "file:///android_asset/$imagen"
    //file:///android_asset/images/nala.png
)