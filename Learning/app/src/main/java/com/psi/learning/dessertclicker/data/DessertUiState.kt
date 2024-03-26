package com.psi.learning.dessertclicker.data

import com.psi.learning.dessertclicker.data.Datasource.dessertList

data class DessertUiState (
    val revenue: Int = 0,
    val dessertsSold: Int = 0,
    val currentDessertIndex: Int = 0,
    val currentDessertPrice: Int = dessertList[currentDessertIndex].price,
    val currentDessertImageId: Int = dessertList[currentDessertIndex].imageId
)