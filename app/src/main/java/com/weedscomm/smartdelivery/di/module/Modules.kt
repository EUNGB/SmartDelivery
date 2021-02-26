package com.weedscomm.smartdelivery.di.module

import android.app.Application
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.weedscomm.smartdelivery.api.CarriersApi
import com.weedscomm.smartdelivery.api.DateTimeConverter
import com.weedscomm.smartdelivery.api.TrackingApi
import com.weedscomm.smartdelivery.data.repository.*
import com.weedscomm.smartdelivery.models.dao.DeliveryDAO
import com.weedscomm.smartdelivery.models.entity.DeliveryEntity
import com.weedscomm.smartdelivery.utils.DeliveryDatabase
import com.weedscomm.smartdelivery.view.DefaultViewModel
import com.weedscomm.smartdelivery.view.add.AddViewModel
import com.weedscomm.smartdelivery.view.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val apiModules = module {
    fun provideCarriesApi(retrofit: Retrofit): CarriersApi =
        retrofit.create(CarriersApi::class.java)

    fun provideTrackingApi(retrofit: Retrofit): TrackingApi =
        retrofit.create(TrackingApi::class.java)

    single { provideCarriesApi(get()) }
    single { provideTrackingApi(get()) }
}

val repository = module {
    factory {
        CarrierRepositoryImpl(get()) as CarrierRepository
    }

    single {
        DeliveryRepositoryImpl((get())) as DeliveryRepository
    }

    factory {
        TrackingRepositoryImpl(get()) as TrackingRepository
    }
}


val viewModel = module {
    viewModel {
        MainViewModel(get(), get(), get(), get())
    }
    viewModel {
        AddViewModel(get(), get(), get())
    }
}

val defaultViewModule = module {
    single {
        DefaultViewModel(get())
    }
}

val databaseModule = module {
    fun provideDatabase(application: Application): DeliveryDatabase {
        return Room.databaseBuilder(application, DeliveryDatabase::class.java, "delivery")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDeliverDAO(database: DeliveryDatabase): DeliveryDAO {
        return database.deliveryDAO()
    }

    fun getAll(dao: DeliveryDAO): List<DeliveryEntity> {
        return dao.getAll()
    }

    single {
        provideDatabase(androidApplication())
    }
    single {
        provideDeliverDAO(get())
    }
    factory {
        getAll(get())
    }

}

const val BASE_URL = "https://apis.tracker.delivery/"

val retrofitModule = module {

    val gson = GsonBuilder().registerTypeHierarchyAdapter(
        DateTime::class.java,
        DateTimeConverter()
    ).setDateFormat("yyyy-MM-dd HH:mm:ssZ").create()


    fun getRetrofitBuild(client: OkHttpClient) = Retrofit.Builder().run {
        baseUrl(BASE_URL)
        client(client)
        addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        addConverterFactory(GsonConverterFactory.create(gson))
        build()
    }


    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
        }

        return builder.build()
    }


    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }

    single { provideLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { getRetrofitBuild(get()) }

}

var appModules = listOf(
    apiModules, repository, viewModel, retrofitModule, defaultViewModule, databaseModule
)