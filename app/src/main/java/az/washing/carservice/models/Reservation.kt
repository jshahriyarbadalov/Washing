package az.washing.carservice.models

import com.google.gson.annotations.SerializedName

data class Reservation(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val user_id: Int?,
    @SerializedName("washing_id")
    val washing_id: Int?,
    @SerializedName("vehicle_type")
    val vehicle_type: String?,
    @SerializedName("service_type")
    val service_type: String?,
    @SerializedName("day")
    val day: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("cancel")
    val cancel: Int?
)
