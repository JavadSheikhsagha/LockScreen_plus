package com.a2mp.lockscreen.Api.api


import com.a2mp.lockscreen.Api.util.Constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstance {

    companion object {

        fun retrofitClient(): SimpleApi {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(SimpleApi::class.java)
        }

        fun retrofitClientRBG(): SimpleApi {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_REMBG)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(SimpleApi::class.java)
        }

        fun retrofitClientWidgetData(): SimpleApi {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(SimpleApi::class.java)
        }

    }

}