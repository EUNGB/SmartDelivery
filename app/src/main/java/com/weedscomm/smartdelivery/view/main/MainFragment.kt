package com.weedscomm.smartdelivery.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.weedscomm.smartdelivery.R
import com.weedscomm.smartdelivery.databinding.FragmentMainBinding
import com.weedscomm.smartdelivery.utils.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModel()

    lateinit var viewAdapter: MainViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigationLivaData.observe(viewLifecycleOwner, Observer {
            when (it) {
                MainViewModel.Navigation.ADD_VIEW -> {
                    requireActivity().findNavController(R.id.nav_host_fragment).navigate(R.id.action_mainFragment_to_addFragment)
                }
                MainViewModel.Navigation.DETAIL_VIEW -> {
                    val directions = MainFragmentDirections.actionMainFragmentToDetailFragment(
                        viewModel.progressLiveData.value!!.toTypedArray()
                    )
                    debug(">>>>> ${viewModel.progressLiveData.value}")
                    findNavController().navigate(directions)
                }
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getAll()
        }

        viewModel.allDeliveriesLiveData.observe(viewLifecycleOwner, {
            debug(it.toString())
            if (it.isNotEmpty()) {
                viewAdapter = MainViewAdapter(it.toMutableList())
                binding.rvDelivery.adapter = viewAdapter
                binding.rvDelivery.layoutManager = LinearLayoutManager(requireActivity())

                viewAdapter.setItemClickListener(object : MainViewAdapter.ItemClickListener {
                    override fun onClick(position: Int, view: View) {
                        val trackId = it[position].trackId
                        val carrierId = it[position].carrierId

                        viewModel.getProgress(trackId, carrierId)
                    }
                })
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}