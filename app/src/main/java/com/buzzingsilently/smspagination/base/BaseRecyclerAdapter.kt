package com.buzzingsilently.smspagination.base

import android.view.View
import android.view.View.OnLongClickListener
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T, VH : BaseRecyclerAdapter<T, VH>.BaseViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>?,
    var listener: RvItemClickListener<T>
) : PagedListAdapter<T, BaseRecyclerAdapter<T, VH>.BaseViewHolder>(diffCallback!!) {

    //interface to listen to item click and long click
    interface RvItemClickListener<M> {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }

    abstract inner class BaseViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val mOnClickListener =
            View.OnClickListener { v ->
                listener.onItemClick(v, adapterPosition)
            }
        private val mOnLongClickListener = OnLongClickListener { view ->
            listener.onItemLongClick(view, adapterPosition)
            true
        }

        abstract fun bind(item: T)

        //registers all clickable view
        fun clickableViews(vararg views: View) {
            for (view in views) {
                view.setOnClickListener(mOnClickListener)
            }
        }

        //registers all long clickable view
        fun longClickableViews(vararg views: View) {
            for (view in views) {
                view.setOnLongClickListener(mOnLongClickListener)
            }
        }
    }
}