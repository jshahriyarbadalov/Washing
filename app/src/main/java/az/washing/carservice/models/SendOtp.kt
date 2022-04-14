package az.washing.carservice.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SendOtp(
    @SerializedName("phone")
    @Expose
    val phone: String
)
