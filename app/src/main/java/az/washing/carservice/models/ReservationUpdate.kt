package az.washing.carservice.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReservationUpdate(
    @SerializedName("washing_id")
    @Expose
    val washing_id: Int?,
    @SerializedName("vehicle_type")
    @Expose
    val vehicle_type: String?,
    @SerializedName("service_type")
    @Expose
    val service_type: String?,
    @SerializedName("day")
    @Expose
    val day: String?,
    @SerializedName("time")
    @Expose
    val time: String?,
    @SerializedName("status")
    @Expose
    val status: Int?
)