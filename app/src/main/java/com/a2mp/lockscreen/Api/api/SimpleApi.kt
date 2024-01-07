package com.a2mp.lockscreen.Api.api

import com.a2mp.lockscreen.Api.Model.ResponseModel
import com.a2mp.lockscreen.Api.Model.ResponseModelLockScreen
import com.a2mp.lockscreen.Widgets.LocationModel
import com.a2mp.lockscreen.Widgets.WidgetsDataModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface SimpleApi {

    @GET("v1/pictures")
    fun getData(
        @Header("Authorization") BearerToken: String
    ): Call<ResponseModel>

    @POST("rembg")
    fun RemBG(
        @Header("Authorization") BearerToken: String,
        @Body rembg: RequestBody
    ): Call<ResponseBody>

    @POST("v1/weather")
    fun getWidgetData(
        @Header("Authorization") BearerToken: String,
        @Body locationModel: LocationModel
    ): Call<WidgetsDataModel>

}
