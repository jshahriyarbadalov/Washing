package az.washing.carservice.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SendOtpResponse(
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("status")
    @Expose
    val status: Int,
    @SerializedName("token")
    @Expose
    val token: String,
)
