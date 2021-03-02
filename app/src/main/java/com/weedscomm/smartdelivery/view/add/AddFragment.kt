package com.weedscomm.smartdelivery.view.add

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.weedscomm.smartdelivery.R
import com.weedscomm.smartdelivery.databinding.FragmentAddBinding
import com.weedscomm.smartdelivery.models.entity.Carriers
import com.weedscomm.smartdelivery.utils.ProgressDialog
import com.weedscomm.smartdelivery.utils.Utils
import com.weedscomm.smartdelivery.view.DefaultViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private val defaultViewModel: DefaultViewModel by viewModel()
    private val viewModel: AddViewModel by viewModel()
    private lateinit var progressDialog: ProgressDialog

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (binding.etCarrier.text.toString() != "" &&
                binding.etProductName.text.toString() != "" &&
                binding.etTrackId.text.toString() != ""
            ) {
                viewModel.isEnabledModeLiveData.postValue(true)
            } else {
                viewModel.isEnabledModeLiveData.postValue(false)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(R.layout.dialog_progress)

        defaultViewModel.carrierListLiveData.value?.let {
            viewModel.carrierListLiveData.value = it
        }
        viewModel.carrierListLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) bindingCarriers(it)
        })

        viewModel.carrierLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.setCarrierId(it)
        })
        binding.etCarrier.inputType = InputType.TYPE_NULL
        binding.etCarrier.addTextChangedListener(textWatcher)
        binding.etTrackId.addTextChangedListener(textWatcher)
        binding.etProductName.addTextChangedListener(textWatcher)
        binding.etCarrier.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                Utils.hideKeyboard(requireActivity())
            }
        }

        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::loading)
        viewModel.navigationLivaData.observe(viewLifecycleOwner, {
            when (it) {
                AddViewModel.Navigation.MAIN_VIEW -> {
                    findNavController().navigate(R.id.action_addFragment_to_mainFragment)
                }
            }
        })
    }

    fun bindingCarriers(list: List<Carriers>) {
        val carries = list.map {
            it.name
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.item_carriers, carries)
        binding.etCarrier.setAdapter(adapter)
    }

    private fun loading(event: AddViewModel.Loading) {
        when (event) {
            AddViewModel.Loading.ON_LOADING -> {
                progressDialog.show(requireActivity().supportFragmentManager, null)
            }
            AddViewModel.Loading.ON_LOADED -> {
                if (progressDialog.isVisible) {
                    progressDialog.dismiss()
                }
            }
        }
    }
}