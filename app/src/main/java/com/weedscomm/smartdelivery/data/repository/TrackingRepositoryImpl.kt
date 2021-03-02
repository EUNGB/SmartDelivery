package com.weedscomm.smartdelivery.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weedscomm.smartdelivery.api.TrackingApi
import com.weedscomm.smartdelivery.models.BaseResponse
import com.weedscomm.smartdelivery.models.ErrorResponse
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
                    val errorBody = it.response()?.errorBody()!!
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val obj = gson.fromJson<ErrorResponse>(errorBody.charStream(), type)

                    callback.onFail(obj.message)
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
                    val errorBody = it.response()?.errorBody()!!
                    val gson = Gson()
                    val type = object : TypeToken<String>() {}.type
                    val obj = gson.fromJson<String>(errorBody.charStream(), type)

                    callback.onFail(obj)
                } else {
                    callback.onError(it)
                }
            }
    }

}