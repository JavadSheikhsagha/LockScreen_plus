package com.a2mp.lockscreen.Api.repository

import com.a2mp.lockscreen.Api.Model.ResponseModel
import com.a2mp.lockscreen.Api.Model.ResponseModelLockScreen
import com.a2mp.lockscreen.Api.api.RetrofitInstance
import com.a2mp.lockscreen.Widgets.LocationModel
import com.a2mp.lockscreen.Widgets.WidgetsDataModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call


class Repository {

    fun GetData(authorization: String): Call<ResponseModel> {
        return RetrofitInstance.retrofitClient()
            .getData(authorization)
    }

    fun RemBG(authorization : String , RemBg: RequestBody): Call<ResponseBody> {
        return RetrofitInstance.retrofitClientRBG()
            .RemBG(authorization , RemBg)
    }

    fun WidgetData(authorization : String , widgetData: LocationModel): Call<WidgetsDataModel> {
        return RetrofitInstance.retrofitClientWidgetData()
            .getWidgetData(authorization , widgetData)
    }

}


