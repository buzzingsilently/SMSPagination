package com.buzzingsilently.smspagination.repository.model

import androidx.recyclerview.widget.DiffUtil
import com.buzzingsilently.smspagination.utility.AppConstant.FORMAT_TIME
import java.text.SimpleDateFormat
import java.util.*

data class SmsModel(
    private val id: Int = -1,
    private val address: String = "",
    private val message: String = "",
    private val isRead: Boolean = false,
    private val time: String = "",
    private val contactName: String = "",
    private var isHeader: Boolean = false, // flag to manage interval based listing in recyclerview
    private var headerInterval: Int = 23
) {
    constructor(isHeader: Boolean, headerInterval: Int) : this() {
        this.isHeader = isHeader
        this.headerInterval = headerInterval
    }

    companion object {
        var DIFF_CALLBACK: DiffUtil.ItemCallback<SmsModel>? =
            object : DiffUtil.ItemCallback<SmsModel>() {
                override fun areItemsTheSame(oldItem: SmsModel, newItem: SmsModel): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: SmsModel, newItem: SmsModel): Boolean {
                    return (oldItem.address == newItem.address
                            && oldItem.message == newItem.message)
                }
            }
    }

    fun getSenderInitial() : String = if (contactName.isEmpty())
        "!"
    else
        contactName.substring(0, 1)

    fun getSenderName() : String = if (contactName.isEmpty())
        address
    else
        contactName

    fun getMessage() : String = if (message.isEmpty())
        "---EMPTY---"
    else
        message

    fun getTime() : Date {
        val date = Date()
        return if (time.isEmpty())
            date
        else {
            val timeFormat = SimpleDateFormat(FORMAT_TIME, Locale.getDefault())
            date.time = time.toLong()
            val timeString = timeFormat.format(date)
            return timeFormat.parse(timeString)!!
        }
    }

    fun isHeader() = isHeader

    fun getHeaderInterval() = headerInterval
}