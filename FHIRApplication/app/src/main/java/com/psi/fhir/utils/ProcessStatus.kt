package com.psi.fhir.utils

sealed class ProcessStatus<T> (
    val data: T? = null,
    var message: String? = null
) {
    class Success<T>( data: T): ProcessStatus<T>(data)
    class Error<T>( message: String): ProcessStatus<T>( message = message )
    class Loading<T>: ProcessStatus<T>()
    class UnSpecified<T>: ProcessStatus<T>()
}