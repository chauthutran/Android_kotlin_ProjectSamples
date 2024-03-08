package com.psi.onlineshop.httpRequest

/**
 * Created by Rohan Jahagirdar on 18-03-2018.
 */
interface FetchCompleteListener {
    fun fetchComplete()
    abstract fun ObjectMapper(): Any
}