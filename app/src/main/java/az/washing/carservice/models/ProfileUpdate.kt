package az.washing.carservice.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileUpdate(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("car_number")
    @Expose
    val car_number: String,
    @SerializedName("email")
    @Expose
    val email: String
)