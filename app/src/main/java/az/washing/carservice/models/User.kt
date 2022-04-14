package az.washing.carservice.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    @Expose
    val id:Int,
    @SerializedName("email")
    @Expose
    val email:String?,
    @SerializedName("name")
    @Expose
    val name:String,
    @SerializedName("car_number")
    @Expose
    val car_number:String,
    @SerializedName("email_verified_at")
    @Expose
    val email_verified_at:String,
    @SerializedName("type")
    @Expose
    val type:String,
    @SerializedName("phone")
    @Expose
    val phone:String,
    @SerializedName("otp_code")
    @Expose
    val otp_code:String,
    @SerializedName("created_at")
    @Expose
    val created_at:String,
    @SerializedName("updated_at")
    @Expose
    val updated_at:String,
)
