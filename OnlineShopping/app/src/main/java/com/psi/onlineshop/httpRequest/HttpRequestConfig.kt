package com.psi.onlineshop.httpRequest

class HttpRequestConfig {
    companion object {
        const val BASE_URL_MONGODB_SERVICE = "http://172.30.1.27:3110"
        val DEFAULT_HEADERS = mapOf("User-Agent" to "HttpRequest","Content-Type" to "application/json", "Accept" to "application/json")

        const val REQUEST_ACTION_ADD_ONE = "addOne"
        const val REQUEST_ACTION_ADD_MANY = "addMany"
        const val REQUEST_ACTION_UPDATE = "update"
        const val REQUEST_ACTION_FIND ="find"
        const val REQUEST_ACTION_DELETE ="delete"

        const val RESPONSE_STATUS_SUCCESS ="success"
        const val RESPONSE_STATUS_ERROR = "error"

        const val COLLECTION_USERS = "users"
        const val COLLECTION_PRODUCTS = "products"
        const val COLLECTION_PRODUCT_LIKES = "productlikes"
        const val COLLECTION_IMAGES = "images"


    }
}