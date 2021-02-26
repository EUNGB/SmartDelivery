package com.weedscomm.smartdelivery.view.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weedscomm.smartdelivery.R
import com.weedscomm.smartdelivery.models.entity.DeliveryEntity
import com.weedscomm.smartdelivery.utils.Configurations.DELIVERED
import com.weedscomm.smartdelivery.utils.Configurations.IN_TRANSIT
import com.weedscomm.smartdelivery.utils.Configurations.OUT_FOR_DELIVERY


class MainViewAdapter(var list: MutableList<DeliveryEntity>) : RecyclerView.Adapter<MainViewAdapter.ViewHolder>() {

    private lateinit var itemClickListener: ItemClickListener

    interface ItemClickListener {
        fun onClick(position: Int, view: View)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_main_delivery, parent, false)
    ) {
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val carrierName: TextView = itemView.findViewById(R.id.tv_delivery_carrier)
        val trackId: TextView = itemView.findViewById(R.id.tv_track_id)
        val fromName: TextView = itemView.findViewById(R.id.tv_delivery_from)
        val toName: TextView = itemView.findViewById(R.id.tv_delivery_to)
        val state: TextView = itemView.findViewById(R.id.tv_delivery_state)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].let {
            with(holder) {
                productName.text = it.productName
                carrierName.text = it.carrierName
                trackId.text = it.trackId
                fromName.text = it.fromName
                toName.text = it.toName
                when (it.status) {
                    IN_TRANSIT -> state.text = "이동중"
                    OUT_FOR_DELIVERY -> state.text = "배송출발"
                    DELIVERED -> state.text = "배송완료"
                }

                itemView.setOnClickListener {
                    itemClickListener.onClick(position, it)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun move(from: Int, to: Int) {
        val prev: DeliveryEntity = list.removeAt(from)
        list.add(if (to > from) to - 1 else to, prev)
        notifyItemMoved(from, to)
    }


}