package com.wasfan.fixfastbuddy.dataClasses
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class requestDataClass(
    val requestId: String,
    val serviceId: String,
    val userPhoneNumber: String,
    val status: String,
    val mechanicPhoneNumber: String?,
    val garageId: String?,
    val date: String,
    val time: String,
    val userInitialAddress: String,
    val userLatitude: Double,
    val userLongitude: Double,
    val mechanicInitialAddress: String?,
    val review: String?,
    val rating: String?,
    val distance: Double,
    val inspectionCost: String,
    val travellingCost: String,
    val firstName: String,
    val lastName: String,
    val latitude: String,
    val longitude: String,
    val profileImagePath: String?,
    val vehicleName: String,
    val type: String
): Parcelable
