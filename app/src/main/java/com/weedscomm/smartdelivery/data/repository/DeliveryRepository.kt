package com.weedscomm.smartdelivery.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.weedscomm.smartdelivery.models.dao.DeliveryDAO
import com.weedscomm.smartdelivery.models.entity.DeliveryEntity


interface DeliveryRepository {
    fun getAll(): List<DeliveryEntity>
    fun insert(delivery: DeliveryEntity)
    fun delete(delivery: DeliveryEntity)
    fun update(delivery: DeliveryEntity)
}

class DeliveryRepositoryImpl(private val deliveryDAO: DeliveryDAO) : DeliveryRepository {


    override fun getAll(): List<DeliveryEntity> {
        return deliveryDAO.getAll()

    }

    override fun insert(delivery: DeliveryEntity) {
        try {
            Thread {
                deliveryDAO.insert(delivery)
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun delete(delivery: DeliveryEntity) {
        try {
            Thread {
                deliveryDAO.delete(delivery)
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun update(delivery: DeliveryEntity) {
        try {
            Thread {
                deliveryDAO.update(delivery)
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}