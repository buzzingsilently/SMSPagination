package com.buzzingsilently.smspagination.view.activity

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buzzingsilently.smspagination.R
import com.buzzingsilently.smspagination.base.BaseRecyclerAdapter
import com.buzzingsilently.smspagination.repository.model.SmsModel
import com.buzzingsilently.smspagination.utility.PermissionCheck
import com.buzzingsilently.smspagination.utility.PermissionCheck.PERMISSION_SMS_LIST
import com.buzzingsilently.smspagination.view.adapter.SmsAdapter
import com.buzzingsilently.smspagination.viewmodel.SmsViewModel
import kotlinx.android.synthetic.main.activity_sms_list.*
import kotlinx.android.synthetic.main.include_toolbar.*

class SmsListActivity : AppCompatActivity(),
    BaseRecyclerAdapter.RvItemClickListener<SmsModel> {

    private lateinit var mAdapter: SmsAdapter
    private lateinit var mViewModel: SmsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_list)
        tvToolbar.setText(R.string.title_sms_list)
        init()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_SMS_LIST -> {
                if (grantResults.isNotEmpty()) {
                    checkSmsPermission()
                }
            }
        }
    }

    private fun init() {
        initView()
        checkSmsPermission()
    }

    private fun checkSmsPermission() {
        if (PermissionCheck.checkPermission(
                this@SmsListActivity,
                arrayOf(Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS,
                    Manifest.permission.RECEIVE_SMS),
                PERMISSION_SMS_LIST
            )
        )
            setObserver()
        else
            changeState()
    }

    private fun initView() {
        mAdapter = SmsAdapter(this, this)
        rvSms.layoutManager =
            LinearLayoutManager(this@SmsListActivity, RecyclerView.VERTICAL, false)
        rvSms.setHasFixedSize(true)
        rvSms.adapter = mAdapter
    }

    private fun setObserver() {
        tvError.text = getString(R.string.info_something_went_wrong)

        mViewModel = ViewModelProviders.of(this).get(SmsViewModel::class.java)

        mViewModel.getSmsList().observe({ this.lifecycle }, {
            mAdapter.submitList(it)
            changeState()
        })
    }

    private fun changeState() {
        if (mAdapter.itemCount > 0) {
            pbLoader.visibility = GONE
            clErrorContainer.visibility = GONE
            rvSms.visibility = VISIBLE
        } else {
            pbLoader.visibility = GONE
            clErrorContainer.visibility = VISIBLE
            rvSms.visibility = GONE
        }
    }

    override fun onItemClick(view: View?, position: Int) {
    }

    override fun onItemLongClick(view: View?, position: Int) {
    }
}
