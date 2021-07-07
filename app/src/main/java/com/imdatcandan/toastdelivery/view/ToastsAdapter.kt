package com.imdatcandan.toastdelivery.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.imdatcandan.toastdelivery.R
import java.util.*

class ToastsAdapter internal constructor(
    private val mContext: Context,
    private val mData: Array<ToastItem>,
    private val toastItemClickListener: ToastItemClickListener
) : RecyclerView.Adapter<ToastsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.toast_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val toastItem = mData[position]
        holder.name.text = toastItem.name
        val locale = mContext.resources.configuration.locale
        val currencySymbol = Currency.getInstance(locale).symbol
        holder.price.text = String.format("%s%s", currencySymbol, toastItem.price)
        val imageRes = getDrawableResWithName(mContext, toastItem.imageName)
        if (imageRes != 0) {
            holder.image.setImageResource(imageRes)
        }
        holder.itemView.setOnClickListener {
            toastItemClickListener.onItemClicked(toastItem)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_toast_name)
        val price: TextView = itemView.findViewById(R.id.tv_toast_price)
        val image: ImageView = itemView.findViewById(R.id.iv_toast)
    }

    private fun getDrawableResWithName(context: Context, name: String): Int {
        val resources = context.resources
        return resources.getIdentifier(name, "drawable", context.packageName)
    }
}