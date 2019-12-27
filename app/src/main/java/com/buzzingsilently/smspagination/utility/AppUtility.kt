package com.buzzingsilently.smspagination.utility

import com.buzzingsilently.smspagination.utility.AppConstant.FORMAT_DATE_TIME
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
        val dateFormat = SimpleDateFormat(FORMAT_DATE_TIME, Locale.getDefault())
        val currentDate = Date()
        currentDate.time = currentDate.time - (TIME_INTERVAL_TWENTY_FOUR)
        val currentDateString = dateFormat.format(currentDate)
        val past24HourDate = dateFormat.parse(currentDateString)
        return past24HourDate?.time ?: 0
    }

    //To find out hour difference between current time and give time
    fun differenceInHour(givenTime: Date) : Int {
        val dateFormat = SimpleDateFormat(FORMAT_DATE_TIME, Locale.getDefault())
        val currentTimeString = dateFormat.format(Date())
        val currentTime = dateFormat.parse(currentTimeString)
        val value = currentTime!!.time.minus(givenTime.time)

        return when {
            value < TIME_INTERVAL_ONE -> 0
            value < TIME_INTERVAL_TWO -> 2
            value < TIME_INTERVAL_THREE -> 3
            value < TIME_INTERVAL_SIX -> 6
            value < TIME_INTERVAL_TWELVE -> 12
            value < TIME_INTERVAL_TWENTY_FOUR -> 24
            else -> 24
        }
    }
}