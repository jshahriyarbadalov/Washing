package az.washing.carservice.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VerifyOtp(
    @SerializedName("phone")
    @Expose
    val phone:String,
    @SerializedName("otp_code")
    @Expose
    val otp_code:String,
)
