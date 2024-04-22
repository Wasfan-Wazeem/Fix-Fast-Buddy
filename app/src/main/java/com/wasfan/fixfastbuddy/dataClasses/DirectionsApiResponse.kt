package com.wasfan.fixfastbuddy.dataClasses

data class DirectionsResponse(val routes: List<Route>)

data class Route(val legs: List<Leg>, val overview_polyline: OverviewPolyline)

data class Leg(val distance: Distance, val duration: Duration)

data class Distance(val text: String)

data class Duration(val text: String)

data class OverviewPolyline(val points: String)
