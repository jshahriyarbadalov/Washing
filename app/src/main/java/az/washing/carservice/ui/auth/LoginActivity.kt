package az.washing.carservice.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import az.washing.carservice.MainActivity
import az.washing.carservice.R
import az.washing.carservice.databinding.ActivityLoginBinding
import az.washing.carservice.utils.NetworkConnection
import az.washing.carservice.utils.StringUtils

class LoginActivity : AppCompatActivity(), AuthView {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private var phone = ""
    private var otpCodes = ""
    private var newPhone = ""
    private val viewModel by viewModels<AuthViewModel>()


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.view = this
        if (!NetworkConnection.isOnline(this)) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("İnternet problemi")
                .setIcon(R.drawable.ic_alert_error)
                .setMessage("İnternet şəbəkəyə qoşulun və yenidən daxil olun")
                .create()
                .show()
        } else {
            viewModel.loginCurrentToken()
            binding.apply {
                etnOtpCode.isEnabled = false
                etnOtpCode.isVisible = false
                btnNext.setOnClickListener {
                    if (phone.isNotEmpty() && etnOtpCode.isVisible) {
                        if (etnOtpCode.text.isNotEmpty()) {
                            otpCodes = etnOtpCode.text.toString()
                            viewModel.verifyOtp(phone, otpCodes)
                        } else {
                            showToastMessage("Otp kodu daxil edin!")
                        }
                    } else {
                        if (etpPhone.text.isNotEmpty()) {
                            newPhone = etpPhone.text.toString()
                            phone = StringUtils.phoneEdit(newPhone)
                            if (StringUtils.getPhone("+$phone")) {
                                pbLoaderLogin.isVisible = true
                                btnNext.isCheckable = false
                                viewModel.sendOtp(phone)
                            } else {
                                showToastMessage("Telefon nömrəni düzgün daxil edin")
                            }
                        } else {
                            showToastMessage("Telefon nömrəni daxil edin!")
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        finishAffinity();
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(
            this@LoginActivity,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun otpCode() = with(binding) {
        etpPhone.isEnabled = false
        etnOtpCode.isEnabled = true
        etnOtpCode.isVisible = true
        btnNext.text = getString(R.string.confirm)
        pbLoaderLogin.isVisible = false
        btnNext.isCheckable = true
    }

    override fun nextPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    override fun goRegister() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}