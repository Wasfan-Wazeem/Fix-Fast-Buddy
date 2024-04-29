package com.wasfan.fixfastbuddy.dataClasses

data class OngoingDataClass(
    //var image: Int,
    var serviceID: String,
    var serviceType: String,
    var vehicleNum: String,
    var address: String,
    var date: String,
    var time: String,
    var status: String?,
    var locationDistance: String
)
