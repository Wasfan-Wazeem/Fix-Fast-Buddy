package com.wasfan.fixfastbuddy.dataClasses

data class CheckStatusDataClass(
    val status : String,
    val serviceId: String,
    val problem: String,
    val inspectionCost:String,
    val travellingCost: String,
    val totalCost: String,
    val cancelledReason: String,
    val serviceCharge: String
)
