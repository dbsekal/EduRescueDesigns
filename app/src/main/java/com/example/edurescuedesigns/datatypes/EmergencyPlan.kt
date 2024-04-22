package com.example.edurescuedesigns.datatypes

data class Coordinates(
    var latitude: String = "0",
    var longitude: String = "0"
)
data class EmergencyPlan(
    var coordinates : Coordinates = Coordinates(),
    var instructions : String = "",
    var is_active : Boolean = false,
    var is_test : Boolean = false
)
