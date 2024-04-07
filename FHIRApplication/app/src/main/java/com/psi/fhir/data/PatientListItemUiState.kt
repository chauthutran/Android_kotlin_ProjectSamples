package com.psi.fhir.data

data class PatientListItemUiState(
    val id: String,
    val name: String,
    val gender: String,
    val dob: String,
    val phone: String,
    val city: String,
    val country: String,
    val isActive: Boolean
)
