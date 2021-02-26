package com.weedscomm.smartdelivery.view.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.weedscomm.smartdelivery.data.repository.CarrierRepository
import com.weedscomm.smartdelivery.data.repository.DeliveryRepository
import com.weedscomm.smartdelivery.data.repository.TrackingRepository
import com.weedscomm.smartdelivery.models.BaseResponse
import com.weedscomm.smartdelivery.models.entity.DeliveryEntity
import com.weedscomm.smartdelivery.models.entity.DeliveryResponseEntity
import com.weedscomm.smartdelivery.utils.SingleLiveEvent

class MainViewModel(
    application: Application,
    private val carrierRepository: CarrierRepository,
    private val deliveryRepository: DeliveryRepository,
    private val trackingRepository: TrackingRepository
) : AndroidViewModel(application) {

    val _navigationLiveData = SingleLiveEvent<Navigation>()
    val navigationLivaData: SingleLiveEvent<Navigation>
        get() = _navigationLiveData

    var allDeliveriesLiveData = MutableLiveData<List<DeliveryEntity>>()
    val progressLiveData = MutableLiveData<ArrayList<DeliveryResponseEntity.Progresses>>()

    fun getAll() {
        allDeliveriesLiveData.postValue(deliveryRepository.getAll())
    }

    fun addTrack() {
        _navigationLiveData.postValue(Navigation.ADD_VIEW)
    }

    fun getProgress(trackId: String, carrierId: String) {
        trackingRepository.getProgress(carrierId, trackId, object : BaseResponse<ArrayList<DeliveryResponseEntity.Progresses>> {
            override fun onSuccess(data: ArrayList<DeliveryResponseEntity.Progresses>) {
                progressLiveData.postValue(data)
                _navigationLiveData.postValue(Navigation.DETAIL_VIEW)
            }

            override fun onFail(descriptor: String) {
            }

            override fun onError(throwable: Throwable) {
            }

            override fun onLoading() {
            }

            override fun onLoaded() {
            }
        })
    }

    enum class Navigation {
        ADD_VIEW,
        DETAIL_VIEW
    }

}