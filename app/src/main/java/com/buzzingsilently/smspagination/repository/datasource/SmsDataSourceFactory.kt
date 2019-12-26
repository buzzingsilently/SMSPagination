package com.buzzingsilently.smspagination.repository.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.buzzingsilently.smspagination.repository.model.SmsModel

class SmsDataSourceFactory(private val dataSource: SmsDataSource)
        : DataSource.Factory<Int, SmsModel>() {

    private var smsDataSourceLiveData = MutableLiveData<SmsDataSource>()

    override fun create(): DataSource<Int, SmsModel> {
        smsDataSourceLiveData.postValue(dataSource)
        return dataSource
    }

}