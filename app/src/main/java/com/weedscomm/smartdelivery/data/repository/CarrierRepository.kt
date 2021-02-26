package com.weedscomm.smartdelivery.data.repository

import com.weedscomm.smartdelivery.models.BaseResponse
import com.weedscomm.smartdelivery.models.entity.Carriers
import io.reactivex.disposables.Disposable

interface CarrierRepository {

    fun getCarriers(callback: BaseResponse<List<Carriers>>): Disposable
}