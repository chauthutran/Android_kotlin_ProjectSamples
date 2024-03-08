package com.psi.onlineshop.httpRequest

class HttpRequestConfig {
    companion object {
        const val BASE_URL_MONGODB_SERVICE = "http://172.30.1.27:3110"
        val DEFAULT_HEADERS = mapOf("User-Agent" to "HttpRequest","Content-Type" to "application/json", "Accept" to "application/json")
    }
}