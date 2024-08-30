package com.developer.shoplistapp

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String? =  null
)

data class GeocodingResponse(
    val results: List<GeocodingResult>,
    val status: String
)

data class GeocodingResult(
    val formatted_address: String
)
