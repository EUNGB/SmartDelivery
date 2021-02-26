package com.weedscomm.smartdelivery.view.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.weedscomm.smartdelivery.data.repository.DeliveryRepository
import com.weedscomm.smartdelivery.data.repository.TrackingRepository
import com.weedscomm.smartdelivery.models.BaseResponse
import com.weedscomm.smartdelivery.models.entity.Carriers
import com.weedscomm.smartdelivery.models.entity.DeliveryEntity
import com.weedscomm.smartdelivery.models.entity.DeliveryResponseEntity
import com.weedscomm.smartdelivery.utils.debug

class AddViewModel(
    application: Application,
    private val trackingRepository: TrackingRepository,
    private val deliveryRepository: DeliveryRepository
) : AndroidViewModel(application) {

    val carrierListLiveData = MutableLiveData<List<Carriers>>()

    val productNameLiveData = MutableLiveData("")
    val trackIdLiveData = MutableLiveData("")
    val carrierLiveData = MutableLiveData("")
    val carrierIdLiveData = MutableLiveData("")

    val isEnabledModeLiveData = MutableLiveData(false)

    fun insertDelivery(delivery: DeliveryEntity) {
        deliveryRepository.insert(delivery)
    }


    fun trackClick() {
        val carrierId = carrierIdLiveData.value ?: ""
        val trackId = trackIdLiveData.value ?: ""

        trackingRepository.trackingDelivery(
            carrierId,
            trackId,
            object : BaseResponse<DeliveryResponseEntity> {
                override fun onSuccess(data: DeliveryResponseEntity) {
                    debug(data.toString())
                    val delivery = DeliveryEntity(
                        fromName = data.from.name?:"발신자",
                        toName = data.to.name?:"수신자",
                        status = data.state.id!!,
                        productName = productNameLiveData.value!!,
                        trackId = trackIdLiveData.value!!,
                        carrierId = carrierIdLiveData.value!!,
                        carrierName = carrierLiveData.value!!
                    )
                    insertDelivery(delivery)
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


    fun setCarrierId(name: String) {
        carrierListLiveData.value?.forEach {
            if (name == it.name) {
                carrierIdLiveData.value = it.id
            }
        }
    }
}