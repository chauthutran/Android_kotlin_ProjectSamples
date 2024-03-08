package com.psi.onlineshop.httpRequest

class HttpRequestConfig {
    companion object {
        const val BASE_URL_MONGODB_SERVICE = "http://172.30.1.27:3110"
        val DEFAULT_HEADERS = mapOf("User-Agent" to "HttpRequest","Content-Type" to "application/json", "Accept" to "application/json")

        const val REQUEST_ACTION_ADD = "add"
        const val REQUEST_ACTION_UPDATE = "update"
        const val REQUEST_ACTION_FIND ="find"

        const val RESPONSE_STATUS_SUCCESS ="success"
        const val RESPONSE_STATUS_ERROR = "error"

        const val SEARCH_OPERATOR_AND = "and"
        const val SEARCH_OPERATOR_OR = "or"

    }
}