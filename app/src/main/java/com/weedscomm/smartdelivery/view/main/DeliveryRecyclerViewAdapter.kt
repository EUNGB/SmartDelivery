package com.weedscomm.smartdelivery.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.weedscomm.smartdelivery.R
import com.weedscomm.smartdelivery.models.entity.DeliveryEntity
import com.weedscomm.smartdelivery.utils.Configurations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DeliveryRecyclerViewAdapter(var list: MutableList<DeliveryEntity>, val viewModel: MainViewModel) :
    RecyclerView.Adapter<DeliveryRecyclerViewAdapter.ViewHolder>() {

    private lateinit var itemClickListener: ItemClickListener
    private val binderHelper = ViewBinderHelper()

    interface ItemClickListener {
        fun onClick(position: Int, view: View)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryRecyclerViewAdapter.ViewHolder {
        binderHelper.setOpenOnlyOne(true)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryRecyclerViewAdapter.ViewHolder, position: Int) {
        if (list.isNotEmpty() && position >= 0 && position < list.size) {

            binderHelper.bind(holder.swipeLayout, list[position].trackId)

            holder.bind(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun saveStates(outState: Bundle?) {
        binderHelper.saveStates(outState)
    }


    fun restoreStates(inState: Bundle?) {
        binderHelper.restoreStates(inState)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val swipeLayout: SwipeRevealLayout = itemView.findViewById(R.id.swipe_layout)
        val frontLayout: FrameLayout = itemView.findViewById(R.id.front_layout)
        val deleteLayout: FrameLayout = itemView.findViewById(R.id.delete_layout)
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val carrierName: TextView = itemView.findViewById(R.id.tv_delivery_carrier)
        val trackId: TextView = itemView.findViewById(R.id.tv_track_id)
        val fromName: TextView = itemView.findViewById(R.id.tv_delivery_from)
        val toName: TextView = itemView.findViewById(R.id.tv_delivery_to)
        val state: TextView = itemView.findViewById(R.id.tv_delivery_state)

        fun bind(position: Int) {
            deleteLayout.setOnClickListener {

                runBlocking {
                    viewModel.deleteDelivery(list[position])
                }
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.getAll()
                }
                list.removeAt(position)
                notifyDataSetChanged()
            }

            frontLayout.setOnClickListener {
                itemClickListener.onClick(position, it)
            }

            list[position].let {
                productName.text = it.productName
                carrierName.text = it.carrierName
                trackId.text = it.trackId
                fromName.text = it.fromName
                toName.text = it.toName
                when (it.status) {
                    Configurations.IN_TRANSIT -> state.text = "이동중"
                    Configurations.OUT_FOR_DELIVERY -> state.text = "배송출발"
                    Configurations.DELIVERED -> state.text = "배송완료"
                }

            }
        }
    }

}