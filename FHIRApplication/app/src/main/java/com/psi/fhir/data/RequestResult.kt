package com.psi.fhir.data

data class RequestResult(
    val success: Boolean,
    val errMsg: String? = null
)
