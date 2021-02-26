package com.weedscomm.smartdelivery.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.weedscomm.smartdelivery.models.dao.DeliveryDAO
import com.weedscomm.smartdelivery.models.entity.DeliveryEntity
import com.weedscomm.smartdelivery.utils.Configurations.TABLE_NAME

@Database(entities = [DeliveryEntity::class], version = 1)
abstract class DeliveryDatabase : RoomDatabase() {

    abstract fun deliveryDAO(): DeliveryDAO

    companion object {
        private var INSTANCE: DeliveryDatabase? = null

        fun getInstance(context: Context): DeliveryDatabase? {
            if (INSTANCE == null) {
                synchronized(DeliveryDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, DeliveryDatabase::class.java, TABLE_NAME)
                        .fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }
}