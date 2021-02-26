package com.weedscomm.smartdelivery.data.repository

import com.weedscomm.smartdelivery.api.CarriersApi
import com.weedscomm.smartdelivery.models.BaseResponse
import com.weedscomm.smartdelivery.models.entity.Carriers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class CarrierRepositoryImpl(private val carriersApi: CarriersApi) : CarrierRepository {

    override fun getCarriers(callback: BaseResponse<List<Carriers>>): Disposable {
        return carriersApi.getCarries()
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


}