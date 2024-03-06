package com.psi.onlineshop

import android.app.Application
import android.content.Context
//import com.mongodb.ConnectionString
//import com.mongodb.MongoClientSettings
//import com.mongodb.ServerApi
//import com.mongodb.ServerApiVersion
//import com.mongodb.kotlin.client.coroutine.MongoClient
//import com.mongodb.kotlin.client.coroutine.MongoDatabase
//import dagger.hilt.android.HiltAndroidApp

class ShoppingApplication: Application() {

    // Replace the placeholder with your MongoDB deployment's connection string
//    private val uri = "mongodb+srv://tranchau:Test1234@cluster0.n0jz7.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
    private val uri = "mongodb://tranchau:Test1234@cluster0.n0jz7.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

//    private lateinit var mongoClient: MongoClient
//    private lateinit var database: MongoDatabase

    // Set the Stable API version on the client


    override fun onCreate() {
        super.onCreate()

//        val serverApi = ServerApi.builder()
//            .version(ServerApiVersion.V1)
//            .build()
//        val settings = MongoClientSettings.builder()
//            .applyConnectionString(ConnectionString(uri))
//            .serverApi(serverApi)
//            .build()
//
//        // Create a new client and connect to the server
//        mongoClient = MongoClient.create(settings)
//        database = mongoClient.getDatabase("onlineshopping")

//        mongoClient = MongoClient.create(uri)
//        val database = mongoClient.getDatabase("onlineshopping")
    }

    companion object {
//       fun getDatabase(context: Context) = (context.applicationContext as ShoppingApplication).database
//
//        fun closeDatabase(context: Context) {
//            (context.applicationContext as ShoppingApplication).mongoClient.close()
//        }
    }
}