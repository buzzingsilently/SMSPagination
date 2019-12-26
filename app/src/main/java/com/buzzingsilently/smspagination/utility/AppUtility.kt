package com.buzzingsilently.smspagination.utility

import com.buzzingsilently.smspagination.utility.AppConstant.FORMAT_DATE
import com.buzzingsilently.smspagination.utility.AppConstant.FORMAT_TIME
import com.buzzingsilently.smspagination.utility.AppConstant.TIME_INTERVAL_ONE
import com.buzzingsilently.smspagination.utility.AppConstant.TIME_INTERVAL_SIX
import com.buzzingsilently.smspagination.utility.AppConstant.TIME_INTERVAL_THREE
import com.buzzingsilently.smspagination.utility.AppConstant.TIME_INTERVAL_TWELVE
import com.buzzingsilently.smspagination.utility.AppConstant.TIME_INTERVAL_TWENTY_FOUR
import com.buzzingsilently.smspagination.utility.AppConstant.TIME_INTERVAL_TWO
import java.text.SimpleDateFormat
import java.util.*

object AppUtility {

    //Get long value of Today/Current Date
    fun getTodayDate() : Long {
        val dateFormat = SimpleDateFormat(FORMAT_DATE, Locale.getDefault())
        val currentDateString = dateFormat.format(Date())
        val currentDate = dateFormat.parse(currentDateString)
        return currentDate?.time ?: 0
    }

    //To find out hour difference between current time and give time
    fun differenceInHour(givenTime: Date) : Int {
        val timeFormat = SimpleDateFormat(FORMAT_TIME, Locale.getDefault())
        val currentTimeString = timeFormat.format(Date())
        val currentTime = timeFormat.parse(currentTimeString)
        val value = currentTime!!.time.minus(givenTime.time)

        return when {
            value < TIME_INTERVAL_ONE -> 0
            value < TIME_INTERVAL_TWO -> 1
            value < TIME_INTERVAL_THREE -> 2
            value < TIME_INTERVAL_SIX -> 3
            value < TIME_INTERVAL_TWELVE -> 6
            value < TIME_INTERVAL_TWENTY_FOUR -> 12
            else -> 24
        }
    }
}