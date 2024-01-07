package com.a2mp.lockscreen.Widgets

data class TopWidgetCategoryItem (var title : String , var icon : Int , var items: MutableList<TopWidgetItems>)

data class TopWidgetItems(var img : Int , var icon : Int , var detail : String , var subtitle : String)