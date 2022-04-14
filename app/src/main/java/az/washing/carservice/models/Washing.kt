package az.washing.carservice.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Washing(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("washing_name")
    @Expose
    val washingName: String?,
    @SerializedName("image")
    @Expose
    val image: String?,
    @SerializedName("owner_name")
    @Expose
    var ownerName: String?,
    @SerializedName("owner_tel")
    @Expose
    val ownerTel: String?,
    @SerializedName("status")
    @Expose
    val status: String?,
    @SerializedName("address")
    @Expose
    val address: String?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String?
)