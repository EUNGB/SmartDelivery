package com.weedscomm.smartdelivery.view.add

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.weedscomm.smartdelivery.data.repository.DeliveryRepository
import com.weedscomm.smartdelivery.data.repository.TrackingRepository
import com.weedscomm.smartdelivery.models.BaseResponse
import com.weedscomm.smartdelivery.models.entity.Carriers
import com.weedscomm.smartdelivery.models.entity.DeliveryEntity
import com.weedscomm.smartdelivery.models.entity.DeliveryResponseEntity
import com.weedscomm.smartdelivery.utils.MaterialDialog
import com.weedscomm.smartdelivery.utils.SingleLiveEvent
import com.weedscomm.smartdelivery.utils.debug
import java.net.UnknownHostException

class AddViewModel(
    application: Application,
    private val trackingRepository: TrackingRepository,
    private val deliveryRepository: DeliveryRepository
) : AndroidViewModel(application) {

    val _navigationLiveData = SingleLiveEvent<Navigation>()
    val navigationLivaData: SingleLiveEvent<Navigation>
        get() = _navigationLiveData

    val loadingLiveData = SingleLiveEvent<Loading>()
    val carrierListLiveData = MutableLiveData<List<Carriers>>()
    val productNameLiveData = MutableLiveData("")
    val trackIdLiveData = MutableLiveData("")
    val carrierLiveData = MutableLiveData("")
    val carrierIdLiveData = MutableLiveData("")
    val isEnabledModeLiveData = MutableLiveData(false)

    private lateinit var confirmDialog: com.afollestad.materialdialogs.MaterialDialog

    val callback = object : MaterialDialog.MaterialCallback {
        override fun onClickOk() {
            confirmDialog.dismiss()
            _navigationLiveData.postValue(Navigation.MAIN_VIEW)
        }

        override fun onClickCancel() {
        }
    }

    fun insertDelivery(delivery: DeliveryEntity) {
        deliveryRepository.insert(delivery)
    }


    fun trackClick(view: View) {
        val carrierId = carrierIdLiveData.value ?: ""
        val trackId = trackIdLiveData.value ?: ""

        trackingRepository.trackingDelivery(
            carrierId,
            trackId,
            object : BaseResponse<DeliveryResponseEntity> {
                override fun onSuccess(data: DeliveryResponseEntity) {
                    debug(data.toString())
                    val delivery = DeliveryEntity(
                        fromName = data.from.name ?: "발신자",
                        toName = data.to.name ?: "수신자",
                        status = data.state.id!!,
                        productName = productNameLiveData.value!!,
                        trackId = trackIdLiveData.value!!,
                        carrierId = carrierIdLiveData.value!!,
                        carrierName = carrierLiveData.value!!
                    )
                    insertDelivery(delivery)
                    confirmDialog = MaterialDialog.createConfirmDialog(view.context, "조회 성공", "해당 상품 조회를 성공했습니다.", callback)!!
                    confirmDialog.show()

                }

                override fun onFail(descriptor: String) {
                    confirmDialog =
                        MaterialDialog.createConfirmDialog(view.context, "조회 실패", descriptor, object : MaterialDialog.MaterialCallback {
                            override fun onClickOk() {
                                confirmDialog.dismiss()
                            }

                            override fun onClickCancel() {}
                        })!!
                    confirmDialog.show()

                }

                override fun onError(throwable: Throwable) {
                    when(throwable) {
                        is UnknownHostException -> {
                            confirmDialog =
                                MaterialDialog.createConfirmDialog(view.context, "네트워크 오류", "네트워크 상태를 확인해주세요.", object : MaterialDialog.MaterialCallback {
                                    override fun onClickOk() {
                                        confirmDialog.dismiss()
                                    }

                                    override fun onClickCancel() {}
                                })!!
                            confirmDialog.show()
                        }
                    }
                }

                override fun onLoading() {
                    loadingLiveData.postValue(Loading.ON_LOADING)
                }

                override fun onLoaded() {
                    loadingLiveData.postValue(Loading.ON_LOADED)
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

    enum class Navigation {
        MAIN_VIEW
    }

    enum class Loading {
        ON_LOADING,
        ON_LOADED
    }
}