package com.psi.learning.days30app.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.psi.learning.R

data class OneDay(
    val dayIdx: Int,
    @StringRes val resAction: Int,
    @StringRes val resDescription: Int,
    @DrawableRes val resImageId: Int
)

object DayList {
    val days = listOf(
        OneDay(
            dayIdx = 1,
            resAction = R.string.day1_action,
            resDescription = R.string.day1_description,
            resImageId = R.drawable.days30_1
        ),
        OneDay(
            dayIdx = 2,
            resAction = R.string.day2_action,
            resDescription = R.string.day12_description,
            resImageId = R.drawable.day2_30_2
        ),
        OneDay(
            dayIdx = 3,
            resAction = R.string.day3_action,
            resDescription = R.string.day3_description,
            resImageId = R.drawable.day2_30_3
        ),
        OneDay(
            dayIdx = 4,
            resAction = R.string.day4_action,
            resDescription = R.string.day4_description,
            resImageId = R.drawable.day2_30_4
        ),
        OneDay(
            dayIdx = 5,
            resAction = R.string.day5_action,
            resDescription = R.string.day5_description,
            resImageId = R.drawable.day2_30_5
        ),
        OneDay(
            dayIdx = 6,
            resAction = R.string.day6_action,
            resDescription = R.string.day6_description,
            resImageId = R.drawable.day2_30_6
        ),
        OneDay(
            dayIdx = 7,
            resAction = R.string.day7_action,
            resDescription = R.string.day7_description,
            resImageId = R.drawable.day2_30_7
        ),
        OneDay(
            dayIdx = 8,
            resAction = R.string.day8_action,
            resDescription = R.string.day8_description,
            resImageId = R.drawable.day2_30_8
        ),
        OneDay(
            dayIdx = 9,
            resAction = R.string.day9_action,
            resDescription = R.string.day9_description,
            resImageId = R.drawable.day2_30_9
        ),
        OneDay(
            dayIdx = 10,
            resAction = R.string.day10_action,
            resDescription = R.string.day10_description,
            resImageId = R.drawable.day2_30_10
        ),
        OneDay(
            dayIdx = 11,
            resAction = R.string.day11_action,
            resDescription = R.string.day11_description,
            resImageId = R.drawable.day2_30_11
        ),
        OneDay(
            dayIdx = 12,
            resAction = R.string.day12_action,
            resDescription = R.string.day12_description,
            resImageId = R.drawable.day2_30_12
        ),
        OneDay(
            dayIdx = 13,
            resAction = R.string.day13_action,
            resDescription = R.string.day13_description,
            resImageId = R.drawable.day2_30_13
        ),
        OneDay(
            dayIdx = 14,
            resAction = R.string.day14_action,
            resDescription = R.string.day14_description,
            resImageId = R.drawable.day2_30_14
        ),
        OneDay(
            dayIdx = 15,
            resAction = R.string.day15_action,
            resDescription = R.string.day15_description,
            resImageId = R.drawable.day2_30_15
        ),
        OneDay(
            dayIdx = 16,
            resAction = R.string.day16_action,
            resDescription = R.string.day16_description,
            resImageId = R.drawable.day2_30_16
        ),
        OneDay(
            dayIdx = 17,
            resAction = R.string.day17_action,
            resDescription = R.string.day12_description,
            resImageId = R.drawable.day2_30_17
        ),
        OneDay(
            dayIdx = 18,
            resAction = R.string.day18_action,
            resDescription = R.string.day18_description,
            resImageId = R.drawable.day2_30_18
        ),
        OneDay(
            dayIdx = 19,
            resAction = R.string.day19_action,
            resDescription = R.string.day19_description,
            resImageId = R.drawable.day2_30_19
        ),
        OneDay(
            dayIdx = 20,
            resAction = R.string.day20_action,
            resDescription = R.string.day20_description,
            resImageId = R.drawable.day2_30_20
        ),
        OneDay(
            dayIdx = 22,
            resAction = R.string.day23_action,
            resDescription = R.string.day22_description,
            resImageId = R.drawable.day2_30_22
        ),
        OneDay(
            dayIdx = 23,
            resAction = R.string.day23_action,
            resDescription = R.string.day23_description,
            resImageId = R.drawable.day2_30_23
        ),
        OneDay(
            dayIdx = 24,
            resAction = R.string.day24_action,
            resDescription = R.string.day24_description,
            resImageId = R.drawable.day2_30_24
        ),
        OneDay(
            dayIdx = 25,
            resAction = R.string.day25_action,
            resDescription = R.string.day25_description,
            resImageId = R.drawable.day2_30_25
        ),
        OneDay(
            dayIdx = 26,
            resAction = R.string.day26_action,
            resDescription = R.string.day26_description,
            resImageId = R.drawable.day2_30_26
        ),
        OneDay(
            dayIdx = 27,
            resAction = R.string.day27_action,
            resDescription = R.string.day27_description,
            resImageId = R.drawable.day2_30_2
        ),
        OneDay(
            dayIdx = 28,
            resAction = R.string.day28_action,
            resDescription = R.string.day28_description,
            resImageId = R.drawable.day2_30_29
        ),
        OneDay(
            dayIdx = 29,
            resAction = R.string.day29_action,
            resDescription = R.string.day29_description,
            resImageId = R.drawable.day2_30_29
        ),
        OneDay(
            dayIdx = 30,
            resAction = R.string.day30_action,
            resDescription = R.string.day30_description,
            resImageId = R.drawable.day2_30_30
        ),
        OneDay(
            dayIdx = 31,
            resAction = R.string.day31_action,
            resDescription = R.string.day31_description,
            resImageId = R.drawable.day2_30_31
        )
    )
}