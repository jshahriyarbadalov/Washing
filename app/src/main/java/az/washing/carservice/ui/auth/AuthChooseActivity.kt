package az.washing.carservice.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.FontCallback.getHandler
import az.washing.carservice.MainActivity
import az.washing.carservice.databinding.ActivityAuthChooseBinding

class AuthChooseActivity : AppCompatActivity() {

    private var _binding: ActivityAuthChooseBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainPage()
    }

    @SuppressLint("RestrictedApi")
    private fun mainPage() {
       Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}