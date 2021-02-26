package com.weedscomm.smartdelivery.data.repository

import com.weedscomm.smartdelivery.api.TrackingApi
import com.weedscomm.smartdelivery.models.BaseResponse
import com.weedscomm.smartdelivery.models.entity.DeliveryEntity
import com.weedscomm.smartdelivery.models.entity.DeliveryResponseEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class TrackingRepositoryImpl(
    private val trackingApi: TrackingApi
) : TrackingRepository {
    override fun trackingDelivery(carrierId: String, trackId: String, callback: BaseResponse<DeliveryResponseEntity>): Disposable {

        return trackingApi.trackingDelivery(carrierId, trackId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { callback.onLoading() }
            .doOnTerminate { callback.onLoaded() }
            .subscribe({
                callback.onSuccess(it)
            }) {
                if (it is HttpException) {
                    callback.onFail(it.message())
                } else {
                    callback.onError(it)
                }
            }
    }

    override fun getProgress(carrierId: String, trackId: String, callback: BaseResponse<ArrayList<DeliveryResponseEntity.Progresses>>): Disposable {
        return trackingApi.trackingDelivery(carrierId, trackId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { callback.onLoading() }
            .doOnTerminate { callback.onLoaded() }
            .subscribe({
                callback.onSuccess(it.progresses)
            }) {
                if (it is HttpException) {
                    callback.onFail(it.message())
                } else {
                    callback.onError(it)
                }
            }
    }

}