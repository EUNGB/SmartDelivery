package com.weedscomm.smartdelivery.data.repository

import com.weedscomm.smartdelivery.models.BaseResponse
import com.weedscomm.smartdelivery.models.entity.DeliveryEntity
import com.weedscomm.smartdelivery.models.entity.DeliveryResponseEntity
import io.reactivex.disposables.Disposable

interface TrackingRepository {

    fun trackingDelivery(carrierId: String, trackId: String, callback: BaseResponse<DeliveryResponseEntity>): Disposable
    fun getProgress(carrierId: String, trackId: String, callback: BaseResponse<ArrayList<DeliveryResponseEntity.Progresses>>): Disposable
}