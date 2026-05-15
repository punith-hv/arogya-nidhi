package com.arogyanidhi.data

import kotlinx.serialization.Serializable

@Serializable
data class Hospital(
    val name: String,
    val district: String,
    val address: String,
    val speciality: String = "Multi-Speciality"
)

@Serializable
data class HospitalResponse(
    val hospitals: List<Hospital>
)