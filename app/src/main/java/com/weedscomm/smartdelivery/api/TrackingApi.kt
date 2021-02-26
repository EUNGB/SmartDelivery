package com.weedscomm.smartdelivery.api

import com.weedscomm.smartdelivery.models.entity.DeliveryResponseEntity
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path

interface TrackingApi {

    @GET("/carriers/{carrier_id}/tracks/{track_id}")
    fun trackingDelivery(
        @Path("carrier_id") carrierId: String,
        @Path("track_id") trackId: String
    ) : Single<DeliveryResponseEntity>
}