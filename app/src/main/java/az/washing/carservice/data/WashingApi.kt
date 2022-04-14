package az.washing.carservice.data

import az.washing.carservice.models.*
import retrofit2.Response
import retrofit2.http.*

interface WashingApi {

    @POST("sendOtp")
    suspend fun sendOtp(@Body send: SendOtp): Response<SendOtpResponse>

    @POST("verifyOtp")
    suspend fun verifyOtp(@Body verify: VerifyOtp): Response<SendOtpResponse>

    @POST("register")
    suspend fun registration(@Body registration: Registration): Response<RegisterResponse>

    @GET("profile")
    suspend fun profile(@Header("Authorization") auth: String): Response<User>

    @POST("logout")
    suspend fun logout(@Header("Authorization") auth: String): Response<List<String>>

    @GET("washings")
    suspend fun washing(@Header("Authorization") auth: String): Response<List<Washing>>

    @GET("reservations")
    suspend fun reservations(@Header("Authorization") auth: String): Response<List<Reservation>>

    @POST("reservations/add")
    suspend fun reservationsAdd(
        @Header("Authorization") auth: String,
        @Body reservation: ReservationAdd
    ): Response<SendOtpResponse>

    @POST("reservations/update")
        suspend fun reservationsUpdate(
        @Header("Authorization") auth: String,
        @Body reservation: ReservationUpdate
    ): Response<SendOtpResponse>
}