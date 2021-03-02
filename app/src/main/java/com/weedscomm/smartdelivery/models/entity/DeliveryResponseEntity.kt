package com.weedscomm.smartdelivery.models.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class DeliveryResponseEntity(
    val from: From,
    val to: To,
    val state: State,
    val progresses: ArrayList<Progresses>,
    val carrier: Carrier
) {


    data class From(
        val name: String?,
        val time: String?
    )

    data class To(
        val name: String?,
        val time: String?
    )

    data class State(
        val id: String?,
        val text: String?
    )

    @Parcelize
    data class Progresses(
        val location: Location,
        val status: Status,
        val time: String,
        val description: String
    ) : Parcelable {

        @Parcelize
        data class Location(
            val name: String
        ) : Parcelable

        @Parcelize
        data class Status(
            val id: String,
            val text: String
        ) : Parcelable
    }

    data class Carrier(
        val name: String?
    )
}
