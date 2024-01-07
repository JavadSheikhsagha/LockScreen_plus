package com.a2mp.lockscreen.Api.Model

import java.util.*

data class ResponseModel(var success : Boolean? , var message : String , var data : List<ResponseModelLockScreen>?)

data class ResponseModelLockScreen(var pictures: List<PictureModel> , var category : CategoryModel)

data class PictureModel(var title : String , var picName : String , var remBgPicName : String)

data class CategoryModel(var name : String)