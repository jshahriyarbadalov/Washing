package az.washing.carservice.ui.auth

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import az.washing.carservice.data.Repository
import az.washing.carservice.models.SendOtp
import az.washing.carservice.models.VerifyOtp
import az.washing.carservice.utils.Constants
import az.washing.carservice.utils.Constants.Companion.SAVE_DATA
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var view: AuthView
    lateinit var regView: RegistrationView
    var sharePreferences: SharedPreferences =
        application.getSharedPreferences(SAVE_DATA, Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = sharePreferences.edit()


    fun sendOtp(phone: String) {
        viewModelScope.launch {
            val sendOtpData = Repository.sendOtp(SendOtp(phone))
            sendOtpData.let {
                if (it.body()?.status == 200) {
                    view.otpCode()
                }
            }
        }
    }

    fun verifyOtp(phone: String, otpCode: String) {
        viewModelScope.launch {
            val verifyCode = Repository.verifyOtp(VerifyOtp(phone, otpCode))
            verifyCode.let {
                when {
                    it.body()?.token != null -> {
                        editor.putString(Constants.USER_TOKEN, it.body()?.token)
                        editor.apply()
                        view.nextPage()
                    }
                    it.body()?.status == 400 -> {
                        view.showErrorMessage("Otp kodu düzgün daxil edin!")
                    }
                    else -> {
                        view.goRegister()
                    }
                }
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun registr(email: String, phone: String, name: String, carNumber: String) {
        viewModelScope.launch {
            val regResponse = Repository.register(email, phone, name, carNumber)
            regResponse.let {
                editor.putString(Constants.USER_TOKEN, it?.token)
                editor.apply()
                if (!it?.token.isNullOrEmpty()) {
                    regView.goLogin()
                }
            }
        }
    }

    fun loginCurrentToken() {
        val userToken = sharePreferences.getString(Constants.USER_TOKEN, "")
        if (!userToken.isNullOrEmpty()) {
            view.nextPage()
        }
    }
}