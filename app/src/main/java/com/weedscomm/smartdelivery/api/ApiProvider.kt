package com.weedscomm.smartdelivery.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val BASE_URL = "https://apis.tracker.delivery/"


fun provideCarriesApi(): CarriersApi = getRetrofitBuild().create(CarriersApi::class.java)


private val gson = GsonBuilder().registerTypeHierarchyAdapter(
    DateTime::class.java,
    DateTimeConverter()
).setDateFormat("yyyy-MM-dd HH:mm:ssZ").create()


private fun getRetrofitBuild() = Retrofit.Builder().run {
    baseUrl(BASE_URL)
    client(provideOkHttpClient(provideLoggingInterceptor()))
    addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    addConverterFactory(GsonConverterFactory.create(gson))
    build()
}



private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
    val builder = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
        readTimeout(60, TimeUnit.SECONDS)
        writeTimeout(60, TimeUnit.SECONDS)
        connectTimeout(60, TimeUnit.SECONDS)
    }

    return builder.build()
}


private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    return interceptor
}