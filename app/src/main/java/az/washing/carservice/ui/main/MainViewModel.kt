package az.washing.carservice.ui.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import az.washing.carservice.data.Repository
import az.washing.carservice.models.User
import az.washing.carservice.models.Washing
import az.washing.carservice.utils.Constants.Companion.SAVE_DATA
import az.washing.carservice.utils.Constants.Companion.USER_TOKEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var _washingList = MutableLiveData<List<Washing>>(emptyList())
    val washing: LiveData<List<Washing>> get() = _washingList
    private var _userInfo = MutableLiveData<User>()
    val userInfo: LiveData<User> get() = _userInfo
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading
    lateinit var view: MainView

    private var sharePreferences: SharedPreferences =
        application.getSharedPreferences(SAVE_DATA, Context.MODE_PRIVATE)
    private val token: String = sharePreferences.getString(USER_TOKEN, "").toString()

    init {
        loadData()
        loadUserInfo()
    }

    private fun loadData() {
        _isLoading.value = true
        val dataLoadingJob = viewModelScope.launch(Dispatchers.IO) {
            val data = Repository.washings("Bearer $token")
            data?.let { _washingList.postValue(it) }
        }

        dataLoadingJob.invokeOnCompletion { _isLoading.postValue(false) }
    }

    @SuppressLint("CommitPrefEdits")
    fun logout() {
        val dataLoadingJob = viewModelScope.launch(Dispatchers.IO) {
            val logout = Repository.logout("Bearer $token")
            logout.let {
                if (it.isSuccessful) {
                    sharePreferences.edit().clear().apply()
                    view.goLogout()
                }
            }
        }
        dataLoadingJob.invokeOnCompletion { _isLoading.postValue(false) }
    }

    private fun loadUserInfo() {
        _isLoading.value = true
        val dataLoadingJob = viewModelScope.launch(Dispatchers.IO) {
            val data = Repository.profile("Bearer $token")
            data?.let { _userInfo.postValue(it) }
        }

        dataLoadingJob.invokeOnCompletion { _isLoading.postValue(false) }
    }
}
