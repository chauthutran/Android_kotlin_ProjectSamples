package com.psi.fhir.data

import java.time.LocalDate

data class PatientData(
    val id: String,
    val resourceId: String,
    val name: String,
    val gender: String,
    val dob: LocalDate?,
    val phone: String,
    val city: String,
    val country: String,
    val isActive: Boolean
)
