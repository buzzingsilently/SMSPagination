package com.buzzingsilently.smspagination.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.buzzingsilently.smspagination.repository.datasource.SmsDataSource
import com.buzzingsilently.smspagination.repository.datasource.SmsDataSourceFactory
import com.buzzingsilently.smspagination.repository.model.SmsModel

class SmsViewModel(application: Application) : AndroidViewModel(application) {

    private var smsListLiveData : LiveData<PagedList<SmsModel>>?

    init {
        val pageConfiguration = PagedList.Config.Builder().setEnablePlaceholders(false)
            .setInitialLoadSizeHint(7)
            .setPageSize(5)
            .setPrefetchDistance(2)
            .build()
        val dataSourceFactory = SmsDataSourceFactory(SmsDataSource(application))
         smsListLiveData = LivePagedListBuilder<Int, SmsModel>(dataSourceFactory, pageConfiguration).build()
    }

    override fun onCleared() {
        smsListLiveData = null
        super.onCleared()
    }

    fun getSmsList() : LiveData<PagedList<SmsModel>> {
        return smsListLiveData!!
    }
}