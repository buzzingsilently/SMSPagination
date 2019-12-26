package com.buzzingsilently.smspagination.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.buzzingsilently.smspagination.R
import com.buzzingsilently.smspagination.base.BaseRecyclerAdapter
import com.buzzingsilently.smspagination.repository.model.SmsModel
import kotlinx.android.synthetic.main.item_sms.view.*

class SmsAdapter(
    private var context: Context,
    listener: RvItemClickListener<SmsModel>
) :
    BaseRecyclerAdapter<SmsModel, SmsAdapter.RepoViewHolder>(SmsModel.DIFF_CALLBACK, listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_sms, parent, false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class RepoViewHolder(private var view: View) : BaseViewHolder(view) {

        init {
            clickableViews(view.llItemContainer)
        }

        @SuppressLint("SetTextI18n")
        override fun bind(item: SmsModel) {
            if (item.isHeader()) {
                view.tvHeader.visibility = VISIBLE
                view.clSms.visibility = GONE
                view.inDivider.visibility = GONE
                view.tvHeader.text = "${item.getHeaderInterval()} hours ago"
            } else {
                view.tvHeader.visibility = GONE
                view.clSms.visibility = VISIBLE
                view.inDivider.visibility = VISIBLE
                view.tvInitial.text = item.getSenderInitial()
                view.tvSender.text = item.getSenderName()
                view.tvSms.text = item.getMessage()
            }
        }
    }
}