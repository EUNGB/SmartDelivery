package com.weedscomm.smartdelivery.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.weedscomm.smartdelivery.R
import com.weedscomm.smartdelivery.databinding.FragmentDetailBinding
import com.weedscomm.smartdelivery.utils.Configurations
import com.weedscomm.smartdelivery.utils.Configurations.AT_PICKUP
import com.weedscomm.smartdelivery.utils.Configurations.DELIVERED
import com.weedscomm.smartdelivery.utils.Configurations.IN_TRANSIT
import com.weedscomm.smartdelivery.utils.Configurations.OUT_FOR_DELIVERY

class DetailFragment : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = args.progress.toCollection(ArrayList())
        binding.timeline.adapter = TimeLineAdapter(list)
        binding.timeline.layoutManager = LinearLayoutManager(requireContext())

        when (list[list.lastIndex].status.id) {
            DELIVERED -> {
                binding.ivDelivered.setBackgroundResource(R.drawable.imageview_bg_primary)
            }
            OUT_FOR_DELIVERY -> {
                binding.ivStartDelivery.setBackgroundResource(R.drawable.imageview_bg_primary)
            }
            IN_TRANSIT -> {
                if (list[list.lastIndex].description.contains("터미널") ||
                    list[list.lastIndex].description.contains("배송지에") ||
                    list[list.lastIndex].description.contains("도착")
                ) {
                    binding.ivWarehouse.setBackgroundResource(R.drawable.imageview_bg_primary)
                } else {
                    binding.ivTransit.setBackgroundResource(R.drawable.imageview_bg_primary)
                }
            }
            AT_PICKUP -> {
                binding.ivReceipt.setBackgroundResource(R.drawable.imageview_bg_primary)
            }
        }
    }
}