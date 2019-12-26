package com.buzzingsilently.smspagination.repository.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.PhoneLookup
import android.provider.Telephony
import androidx.paging.PositionalDataSource
import com.buzzingsilently.smspagination.repository.model.SmsModel
import com.buzzingsilently.smspagination.utility.AppConstant
import com.buzzingsilently.smspagination.utility.AppConstant.SQL_AND
import com.buzzingsilently.smspagination.utility.AppConstant.SQL_LIMIT
import com.buzzingsilently.smspagination.utility.AppUtility

class SmsDataSource(private val context: Context) : PositionalDataSource<SmsModel>() {

    private var referenceList = mutableListOf<SmsModel>()
    private var firstMessageId = 0

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<SmsModel>) {
        firstMessageId = getFirstSmsId()
        val initialList = getSmsList(firstMessageId, params.requestedLoadSize)
        callback.onResult(initialList, 0, initialList.size)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<SmsModel>) {
        callback.onResult(getSmsList(firstMessageId - params.startPosition, params.loadSize))
    }

    //Get sms id to start a item base pagination
    private fun getFirstSmsId(): Int {
        var firstId = -1
        val smsUri = Uri.parse(AppConstant.URI_SMS)
        val order =
            StringBuilder(Telephony.Sms.DATE).append(" ").append(AppConstant.SQL_DESC).append(" ")
                .append(SQL_LIMIT).append(" ").append("1").toString()
        val query = StringBuilder(StringBuilder(Telephony.Sms.DATE)).append(" > ")
            .append(AppUtility.getTodayDate()).toString()
        val contentResolver = context.contentResolver
        val cursor: Cursor? =
            contentResolver.query(smsUri, arrayOf(Telephony.Sms._ID), query, null, order)
        if (cursor == null) {
            return firstId
        } else {
            if (cursor.moveToFirst()) {
                firstId = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms._ID))
            }
            if (!cursor.isClosed) {
                cursor.close()
            }
        }
        return firstId
    }

    //Get sms list based on id and requested load size
    private fun getSmsList(id: Int, requestedLoadSize: Int): MutableList<SmsModel> {
        val smsList: MutableList<SmsModel> = ArrayList()
        val smsUri = Uri.parse(AppConstant.URI_SMS)
        val order =
            StringBuilder(Telephony.Sms.DATE).append(" ").append(AppConstant.SQL_DESC).append(" ")
                .append(SQL_LIMIT).append(" ").append(requestedLoadSize).toString()
        val query = StringBuilder(StringBuilder(Telephony.Sms.DATE)).append(" > ")
            .append(AppUtility.getTodayDate()).append(" ").append(SQL_AND).append(" ")
            .append(Telephony.Sms._ID)
            .append(" < ").append(id).toString()
        val contentResolver = context.contentResolver
        val cursor: Cursor? =
            contentResolver.query(smsUri, null, query, null, order)
        if (cursor == null) {
            return smsList
        } else {
            val totalSMS: Int = cursor.count
            if (cursor.moveToFirst()) {
                for (i in 0 until totalSMS) {
                    val address =
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val objSms = SmsModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms._ID)),
                        address,
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)),
                        cursor.getString(cursor.getColumnIndex(Telephony.Sms.READ)) == "1",
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE)),
                        getContactName(address))
                    smsList.add(objSms)
                    cursor.moveToNext()
                }
                if (!cursor.isClosed) {
                    cursor.close()
                }
            }
        }
        return modifiedListWithHeader(smsList)
    }

    //Find contact name from phonebook using address of a message
    @SuppressLint("Recycle")
    private fun getContactName(phoneNumber: String?): String {
        val phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val contentResolver = context.contentResolver
        val cursor =
            contentResolver.query(phoneUri, arrayOf(PhoneLookup.DISPLAY_NAME), null, null, null)
                ?: return ""
        var contactName = ""
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME))
        }
        if (!cursor.isClosed) {
            cursor.close()
        }
        return contactName
    }

    //adds header if require and updates list accordingly
    private fun modifiedListWithHeader(list: MutableList<SmsModel>): MutableList<SmsModel> {
        val newList = mutableListOf<SmsModel>()
        var headerSmsModel: SmsModel
        list.forEach {
            when (AppUtility.differenceInHour(it.getTime())) {
                0 -> {
                    headerSmsModel = SmsModel(true, 0)
                }
                1 -> {
                    headerSmsModel = SmsModel(true, 1)
                }
                2 -> {
                    headerSmsModel = SmsModel(true, 2)
                }
                3 -> {
                    headerSmsModel = SmsModel(true, 3)
                }
                6 -> {
                    headerSmsModel = SmsModel(true, 6)
                }
                12 -> {
                    headerSmsModel = SmsModel(true, 12)
                }
                else -> {
                    headerSmsModel = SmsModel(true, 24)
                }
            }
            if (!referenceList.contains(headerSmsModel)) {
                referenceList.add(headerSmsModel)
                newList.add(headerSmsModel)
            }
            newList.add(it)
        }
        return newList
    }
}