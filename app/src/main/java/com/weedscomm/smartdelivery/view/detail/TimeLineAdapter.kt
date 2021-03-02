package com.weedscomm.smartdelivery.view.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.weedscomm.smartdelivery.R
import com.weedscomm.smartdelivery.databinding.ItemTimeLineBinding
import com.weedscomm.smartdelivery.models.entity.DeliveryResponseEntity
import com.weedscomm.smartdelivery.utils.Configurations.AT_PICKUP
import com.weedscomm.smartdelivery.utils.Configurations.DELIVERED
import com.weedscomm.smartdelivery.utils.Configurations.IN_TRANSIT
import com.weedscomm.smartdelivery.utils.Configurations.OUT_FOR_DELIVERY
import com.weedscomm.smartdelivery.utils.debug
import org.w3c.dom.Text

class TimeLineAdapter(private var list: ArrayList<DeliveryResponseEntity.Progresses>) : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    inner class TimeLineViewHolder(private var binding: ItemTimeLineBinding, viewType: Int) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.timeline.initLine(viewType)
        }

        fun bind(progresses: DeliveryResponseEntity.Progresses) {
            binding.progress = progresses
        }
    }

    override fun getItemViewType(position: Int) =
        TimelineView.getTimeLineViewType(position, itemCount)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<ItemTimeLineBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_time_line,
            parent,
            false
        ).let {
            TimeLineViewHolder(it, viewType)
        }


    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.bind(list[list.size.minus(1) - position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}