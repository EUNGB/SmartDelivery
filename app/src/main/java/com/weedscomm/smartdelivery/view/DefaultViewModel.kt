package com.weedscomm.smartdelivery.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weedscomm.smartdelivery.data.repository.CarrierRepository
import com.weedscomm.smartdelivery.models.BaseResponse
import com.weedscomm.smartdelivery.models.entity.Carriers
import com.weedscomm.smartdelivery.utils.debug

class DefaultViewModel(
    private val carrierRepository: CarrierRepository
) : ViewModel() {

    val carrierListLiveData = MutableLiveData<List<Carriers>>()

    fun getCarriers() {
        carrierRepository.getCarriers(object : BaseResponse<List<Carriers>> {
            override fun onSuccess(data: List<Carriers>) {
                debug(data.toString())
                carrierListLiveData.value = data
            }

            override fun onFail(descriptor: String) {
                debug(descriptor)
            }

            override fun onError(throwable: Throwable) {
            }

            override fun onLoading() {
            }

            override fun onLoaded() {
            }

        })
    }

}