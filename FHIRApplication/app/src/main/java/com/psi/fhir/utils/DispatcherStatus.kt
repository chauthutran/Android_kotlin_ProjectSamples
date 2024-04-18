package com.psi.fhir.utils

sealed class DispatcherStatus<T> (
    val data: T? = null,
    var message: String? = null
) {
    class Success<T>( data: T): DispatcherStatus<T>(data)
    class Error<T>( message: String): DispatcherStatus<T>( message = message )
    class Loading<T>: DispatcherStatus<T>()
    class UnSpecified<T>: DispatcherStatus<T>()
}