package az.washing.carservice.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import az.washing.carservice.R
import az.washing.carservice.databinding.ActivityRegistrationBinding
import az.washing.carservice.utils.NetworkConnection
import az.washing.carservice.utils.StringUtils

class RegistrationActivity : AppCompatActivity(), RegistrationView {
    private var _binding: ActivityRegistrationBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()
    var phone = ""
    var name = ""
    var carNumber = ""
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.regView = this
        if (!NetworkConnection.isOnline(this)) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("İnternet problemi!")
                .setIcon(R.drawable.ic_alert_error)
                .setMessage("İnternet şəbəkəyə qoşulun və yenidən daxil olun")
                .create()
                .show()
        }
        binding.btnNext.setOnClickListener { addRegistry() }
    }


    private fun addRegistry() {
        binding.apply {
            phone = etpPhone.text.toString()
            name = etpnName.text.toString()
            carNumber = etpnCarNumber.text.toString()
            email = etEmail.text.toString()
            if (phone.isNotEmpty() && name.isNotEmpty() && carNumber.isNotEmpty()) {
                val newPhone = StringUtils.phoneEdit(phone)
                if (StringUtils.getPhone("+${newPhone}")) {
                    viewModel.registr(email, newPhone, name, carNumber)
                } else {
                    showToastMessage("Telefon nomresin düzgün daxil edin!")
                }
            } else {
                etpPhone.setBackgroundColor(
                    ContextCompat.getColor(
                        this@RegistrationActivity,
                        R.color.red
                    )
                )
                etpnCarNumber.setBackgroundColor(
                    ContextCompat.getColor(
                        this@RegistrationActivity,
                        R.color.red
                    )
                )
                etpnName.setBackgroundColor(
                    ContextCompat.getColor(
                        this@RegistrationActivity,
                        R.color.red
                    )
                )
                showToastMessage("Xanaları doldurun!")
            }
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(
            this@RegistrationActivity,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun goLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


