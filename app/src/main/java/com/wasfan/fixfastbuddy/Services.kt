package com.wasfan.fixfastbuddy

import com.wasfan.fixfastbuddy.dataClasses.ServicesDataClass

object Services {
    val servicesList = arrayListOf(
        ServicesDataClass(R.drawable.services_tyre, "Tyre", "101"),
        ServicesDataClass(R.drawable.services_engine, "Engine", "102"),
        ServicesDataClass(R.drawable.services_tow_truck, "Towing", "103"),
        ServicesDataClass(R.drawable.services_battery, "Battery", "104"),
        ServicesDataClass(R.drawable.services_lockout, "Lockout", "105"),
        ServicesDataClass(R.drawable.services_fuel_delivery, "Fuel Delivery", "106"),
        ServicesDataClass(R.drawable.services_ev_charging, "Electrical", "107"),
        ServicesDataClass(R.drawable.services_winching, "Winching", "108"),
        ServicesDataClass(R.drawable.services_electrical, "EV Charging", "109"),
        ServicesDataClass(R.drawable.services_unknown, "Unknown", "110")
    )
}