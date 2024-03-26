package com.psi.learning.gridlayput.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class GridItem(
    @StringRes val title: Int,
    val availableCourses: Int,
    @DrawableRes val imageResourceId: Int
)


