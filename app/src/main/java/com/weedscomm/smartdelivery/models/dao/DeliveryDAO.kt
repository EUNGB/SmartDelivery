package com.weedscomm.smartdelivery.models.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.weedscomm.smartdelivery.models.entity.DeliveryEntity
import com.weedscomm.smartdelivery.utils.Configurations.TABLE_NAME

@Dao
interface DeliveryDAO {

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id ASC")
    fun getAll() : List<DeliveryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deliver: DeliveryEntity)

    @Delete
    fun delete(deliver: DeliveryEntity)

    @Update
    fun update(deliver: DeliveryEntity)
}