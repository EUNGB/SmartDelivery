package com.weedscomm.smartdelivery.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weedscomm.smartdelivery.api.CarriersApi
import com.weedscomm.smartdelivery.models.BaseResponse
import com.weedscomm.smartdelivery.models.ErrorResponse
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


}