package com.example.petcare2.home.ui.composables

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PetCareOutlinedTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    placeholder: String = "",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChanged(it) },
        placeholder = { Text(text = placeholder) },
        singleLine = true,
    )
}