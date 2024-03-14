package com.psi.onlineshop.utils

import com.psi.onlineshop.adapters.SliderImageAdapter
import com.smarteist.autoimageslider.SliderView

fun SliderView.setupSliderImages(imageUrlList: ArrayList<String>, autoCycle: Boolean) {

    // slider adapter and adding our list to it.
    val sliderAdapter = SliderImageAdapter(imageUrlList)
    // Set auto cycle direction for our slider view from left to right.
    autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
    // Set auto cycle direction for our slider view from left to right.
    autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR

    // on below line we are setting adapter for our slider.
    setSliderAdapter(sliderAdapter)

    if( autoCycle )
    {
        // Set scroll time in seconds for our slider view.
        scrollTimeInSec = 3

        // Set auto cycle to true to auto slide our items.
        isAutoCycle = autoCycle

        // Call start auto cycle to start our cycle.
        startAutoCycle()
    }

}