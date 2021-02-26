package com.weedscomm.smartdelivery.api

import com.weedscomm.smartdelivery.models.entity.Carriers
import io.reactivex.Single
import retrofit2.http.GET

interface CarriersApi {

    @GET("/carriers")
    fun getCarries(): Single<List<Carriers>>

}