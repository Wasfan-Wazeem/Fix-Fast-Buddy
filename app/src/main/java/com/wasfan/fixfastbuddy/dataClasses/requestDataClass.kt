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
    var review: String?,
    var rating: Float?,
    val distance: Double,
    val inspectionCost: Double,
    val travellingCost: Double?,
    val totalCost: Double,
    val firstName: String,
    val lastName: String,
    var latitude: Double,
    var longitude: Double,
    val profileImagePath: String?,
    val vehicleName: String,
    val type: String,
    var cancelledReason: String?,
    var description: String?
): Parcelable
