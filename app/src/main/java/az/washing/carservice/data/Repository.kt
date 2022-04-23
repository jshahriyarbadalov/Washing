package az.washing.carservice.data

import az.washing.carservice.models.*
import retrofit2.Response

object Repository {

    suspend fun sendOtp(phone: SendOtp): Response<SendOtpResponse> {
        return ApiService.washingApi.sendOtp(phone)
    }

    suspend fun verifyOtp(verifyOtp: VerifyOtp): Response<SendOtpResponse> {
        return ApiService.washingApi.verifyOtp(verifyOtp)
    }

    suspend fun register(
        email: String,
        phone: String,
        name: String,
        carNumber: String
    ): RegisterResponse? {
        return ApiService.washingApi.registration(Registration(email, phone, name, carNumber))
            .body()
    }

    suspend fun profile(auth: String): User? {
        return ApiService.washingApi.profile(auth).body()
    }

    suspend fun logout(auth: String): Response<List<String>> {
        return ApiService.washingApi.logout(auth)
    }

    suspend fun washings(auth: String): List<Washing>? {
        return ApiService.washingApi.washing(auth).body()
    }

    suspend fun reservation(token: String): List<Reservation>? {
        return ApiService.washingApi.reservations(token).body()
    }

    suspend fun reservationAdd(auth: String, add: ReservationAdd): Response<SendOtpResponse> {
        return ApiService.washingApi.reservationsAdd(auth, add)
    }

    suspend fun reservationUpdate(
        auth: String,
        update: ReservationUpdate
    ): Response<SendOtpResponse> {
        return ApiService.washingApi.reservationsUpdate(auth, update)
    }

    suspend fun getTimes(id: Int, day: String): Response<List<String>> {
        return ApiService.washingApi.getTimes(id, day)
    }
}