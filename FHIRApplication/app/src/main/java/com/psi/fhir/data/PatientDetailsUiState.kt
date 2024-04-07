package com.psi.fhir.data

import java.time.LocalDate

data class PatientDetailsUiState(
    val id: String = "",
    val name: String = "",
    val gender: String = "",
    val dob: LocalDate? = null,
    val phone: String = "",
    val city: String = "",
    val country: String = "",
//    val observations: List<ObservationListItem>?
)