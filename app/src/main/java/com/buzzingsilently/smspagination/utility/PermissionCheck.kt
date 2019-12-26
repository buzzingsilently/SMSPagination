package com.buzzingsilently.smspagination.utility

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionCheck {

    const val PERMISSION_SMS_LIST = 10001

    fun checkPermission(activity: Activity, permission: Array<String>, requestCode: Int) : Boolean {
        val pendingRequestList = mutableListOf<String>()
        permission.forEach {
            if (ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED)
                pendingRequestList.add(it)
        }
        return if (pendingRequestList.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, pendingRequestList.toTypedArray(), requestCode)
            false
        } else
            true
    }
}