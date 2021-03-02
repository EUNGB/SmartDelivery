package com.weedscomm.smartdelivery.view.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.weedscomm.smartdelivery.data.repository.CarrierRepository
import com.weedscomm.smartdelivery.data.repository.DeliveryRepository
import com.weedscomm.smartdelivery.data.repository.TrackingRepository
import com.weedscomm.smartdelivery.models.BaseResponse
import com.weedscomm.smartdelivery.models.entity.DeliveryEntity
import com.weedscomm.smartdelivery.models.entity.DeliveryResponseEntity
import com.weedscomm.smartdelivery.utils.MaterialDialog
import com.weedscomm.smartdelivery.utils.SingleLiveEvent
import com.weedscomm.smartdelivery.utils.debug
import java.net.UnknownHostException

class MainViewModel(
    application: Application,
    private val carrierRepository: CarrierRepository,
    private val deliveryRepository: DeliveryRepository,
    private val trackingRepository: TrackingRepository
) : AndroidViewModel(application) {

    private lateinit var confirmDialog: com.afollestad.materialdialogs.MaterialDialog

    val _navigationLiveData = SingleLiveEvent<Navigation>()
    val navigationLivaData: SingleLiveEvent<Navigation>
        get() = _navigationLiveData
    val loadingLiveData = SingleLiveEvent<Loading>()
    var allDeliveriesLiveData = MutableLiveData<List<DeliveryEntity>>()
    val progressLiveData = MutableLiveData<ArrayList<DeliveryResponseEntity.Progresses>>()

    fun getAll() {
        allDeliveriesLiveData.postValue(deliveryRepository.getAll())
    }

    fun deleteDelivery(delivery: DeliveryEntity) {
        deliveryRepository.delete(delivery)
    }

    fun addTrack() {
        _navigationLiveData.postValue(Navigation.ADD_VIEW)
    }

    fun getProgress(trackId: String, carrierId: String, context: Context) {
        trackingRepository.getProgress(carrierId, trackId, object : BaseResponse<ArrayList<DeliveryResponseEntity.Progresses>> {
            override fun onSuccess(data: ArrayList<DeliveryResponseEntity.Progresses>) {
                progressLiveData.postValue(data)
                _navigationLiveData.postValue(Navigation.DETAIL_VIEW)
            }

            override fun onFail(descriptor: String) {
                confirmDialog =
                    MaterialDialog.createConfirmDialog(context, "조회 실패", descriptor, object : MaterialDialog.MaterialCallback {
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
                            MaterialDialog.createConfirmDialog(context, "네트워크 오류", "네트워크 상태를 확인해주세요.", object : MaterialDialog.MaterialCallback {
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

    enum class Navigation {
        ADD_VIEW,
        DETAIL_VIEW
    }

    enum class Loading {
        ON_LOADING,
        ON_LOADED
    }

}