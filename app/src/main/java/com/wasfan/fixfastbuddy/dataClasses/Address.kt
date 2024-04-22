package com.wasfan.fixfastbuddy.dataClasses

data class Address(
    val street: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)
