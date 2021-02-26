package com.weedscomm.smartdelivery.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weedscomm.smartdelivery.utils.Configurations.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class DeliveryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val trackId: String,
    val carrierId: String,
    val carrierName: String,
    val fromName: String,
    val toName: String,
    val status: String,
    val productName: String
)