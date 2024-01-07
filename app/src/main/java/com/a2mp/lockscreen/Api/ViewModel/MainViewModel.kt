package com.a2mp.lockscreen.Api.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a2mp.lockscreen.Api.Model.ResponseModel
import com.a2mp.lockscreen.Api.Model.ResponseModelLockScreen
import com.a2mp.lockscreen.Api.repository.Repository
import com.a2mp.lockscreen.Widgets.LocationModel
import com.a2mp.lockscreen.Widgets.WidgetsDataModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel() : ViewModel() {

    val repository: Repository = Repository()


    fun GetData(LockScreen: String) : LiveData<ResponseModel> {
        val myRepository: MutableLiveData<ResponseModel> = MutableLiveData()
        val response = repository.GetData(LockScreen)
        try {
            response.enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    myRepository.value = response.body()
                    Log.i("TAGSSSSS", "onResponse: ${myRepository.value}        ${response.code()}")
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Log.i("LOG123", "onFailure: ${t.message} ${t.localizedMessage}")

                }
            })
        }catch (e : Exception){

        }
        return myRepository
    }


    fun RemoveBG(authorization : String , RemBg: RequestBody) : LiveData<ResponseBody?> {
        val myRepository: MutableLiveData<ResponseBody?> = MutableLiveData()
        val response = repository.RemBG( authorization , RemBg)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                myRepository.value = response.body()
                Log.i("TAGSSSSS", "onResponse: ${myRepository.value}")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.i("LOG123", "onFailure: ${t.message}")
            }
        })
        return myRepository
    }

    fun WidgetData(authorization : String , WidgetData: LocationModel) : LiveData<WidgetsDataModel?> {
        val myRepository: MutableLiveData<WidgetsDataModel?> = MutableLiveData()
        val response = repository.WidgetData( authorization , WidgetData)
            response.enqueue(object : Callback<WidgetsDataModel> {
                override fun onResponse(
                    call: Call<WidgetsDataModel>,
                    response: Response<WidgetsDataModel>
                ) {
                    response?.body()?.let {
                        myRepository.value = response.body()
                        Log.i("moooooooooooooooooooooo", "onResponse: ${myRepository.value}")
                        Log.i("moooooooooooooooooooooo", "onResponse: ${response}")
                    }
                }

                override fun onFailure(call: Call<WidgetsDataModel>, t: Throwable) {
                    Log.i("moooooooooooooooooooooo1", "onFailure: ${t.message}")
                }
            })

        return myRepository
    }


}