package az.washing.carservice.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("user")
    @Expose
    val user: User,
    @SerializedName("token")
    @Expose
    val token: String,
)
