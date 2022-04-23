package az.washing.carservice.ui.order

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import az.washing.carservice.data.Repository
import az.washing.carservice.models.Reservation
import az.washing.carservice.models.ReservationAdd
import az.washing.carservice.models.ReservationUpdate
import az.washing.carservice.models.Washing
import az.washing.carservice.utils.Constants.Companion.SAVE_DATA
import az.washing.carservice.utils.Constants.Companion.USER_TOKEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private var sharePreferences: SharedPreferences =
        application.getSharedPreferences(SAVE_DATA, Context.MODE_PRIVATE)
    private val token: String = sharePreferences.getString(USER_TOKEN, "").toString()
    private var _reservations = MutableLiveData<List<Reservation>>(emptyList())
    val reservations: LiveData<List<Reservation>> get() = _reservations
    private var _washingList = MutableLiveData<List<Washing>>(emptyList())
    val washings: LiveData<List<Washing>> get() = _washingList
    private var _bookedOrderList = MutableLiveData<List<String>>(emptyList())
    val bookedOrders: LiveData<List<String>> get() = _bookedOrderList
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading


    lateinit var view: BookingView
    lateinit var viewOrder: OrderView


    init {
        loadWashing()
        loadDataOrder()
        //  reservationTime()
    }

    private fun loadWashing() {
        _isLoading.value = true
        val dataLoadingJob = viewModelScope.launch(Dispatchers.IO) {
            val washingData =
                Repository.washings("Bearer $token")
            washingData?.let { _washingList.postValue(it) }
        }
        dataLoadingJob.invokeOnCompletion { _isLoading.postValue(false) }
    }

    private fun loadDataOrder() {
        _isLoading.value = true
        val dataLoadingJob = viewModelScope.launch(Dispatchers.IO) {
            val reservationData =
                Repository.reservation("Bearer $token")
            reservationData?.let { _reservations.postValue(it) }

        }
        dataLoadingJob.invokeOnCompletion { _isLoading.postValue(false) }
    }

    fun addReservation(add: ReservationAdd) {
        viewModelScope.launch {
            val reservationData = Repository.reservationAdd(
                "Bearer $token",
                add
            )
            reservationData.let {
                if (it.code() == 200) {
                    view.showSuccessMessage("Sifarişiniz uğurla qeydə alındı!")
                    view.goBack()
                } else {
                    it.message()
                    view.showSuccessMessage("Sifarişiniz qeydə alınmadı. Yenidən cəht edin.")
                }
            }
        }
    }

    fun updateReservation(update: ReservationUpdate) {
        viewModelScope.launch {
            val reservationUpdate = Repository.reservationUpdate(
                "Bearer $token",
                update
            )

            reservationUpdate.let {
                if (it.body()?.status == 200) {
                    view.showSuccessMessage("Sifarişiniz uğurla yeniləndi!")
                    view.goBack()
                } else {
                    it.message()
                    view.showSuccessMessage("Sifarişiniz yenilənmədi. Yenidən cəht edin.")
                }
            }
        }
    }

    fun cancelReservation(update: ReservationUpdate) {
        viewModelScope.launch {
            val reservationUpdate = Repository.reservationUpdate(
                "Bearer $token",
                update
            )

            reservationUpdate.let {
                if (it.body()?.status == 200) {
                    viewOrder.showSuccessMessage("Sifarişiniz uğurla ləğv olundu!")
                } else {
                    it.message()
                    viewOrder.showSuccessMessage("Sifarişiniz ləğv olunmadı. Yenidən cəht edin.")
                }
            }
        }
    }

    fun getTimesData(id: Int, day: String) {
        viewModelScope.launch {
            val repository = Repository.getTimes(id, day)
            _bookedOrderList.postValue(repository.body())

            if (!repository.body().isNullOrEmpty()) {
                view.filterForTime()
            }
        }
    }

}