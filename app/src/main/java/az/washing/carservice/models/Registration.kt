package az.washing.carservice.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Registration(
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("phone")
    @Expose
    val phone: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("car_number")
    @Expose
    val car_number: String
)
