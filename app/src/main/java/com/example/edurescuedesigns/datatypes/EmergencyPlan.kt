package com.example.edurescuedesigns.datatypes

data class EmergencyPlan(
    var coordinates : String = "",
    var instructions : String = "",
    var is_active : Boolean = false,
    var is_test : Boolean = false
)
