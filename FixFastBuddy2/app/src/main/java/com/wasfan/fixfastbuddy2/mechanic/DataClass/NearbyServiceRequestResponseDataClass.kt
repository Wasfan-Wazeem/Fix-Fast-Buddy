package com.wasfan.fixfastbuddy2.mechanic.DataClass

data class NearbyServiceRequestResponseDataClass(
    val success: Boolean,
    val message: String,
    val data: List<ServiceRequestDataClass>
)
