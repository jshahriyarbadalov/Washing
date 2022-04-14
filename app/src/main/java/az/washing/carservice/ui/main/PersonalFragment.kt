package az.washing.carservice.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import az.washing.carservice.databinding.FragmentPersonalBinding
import az.washing.carservice.ui.auth.LoginActivity


class PersonalFragment : Fragment(), MainView {

    private var _binding: FragmentPersonalBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.view = this
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.apply {
                pbLoader.isVisible = it
                tvLoaderText.isVisible = it
            }
        })
        personalInfo()
        binding.btnExit.setOnClickListener { viewModel.logout() }
    }

    override fun goLogout() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)

    }

    override fun goBooking(id: Int) {
        TODO("Not yet implemented")
    }

    override fun goMap(address: String) {
        TODO("Not yet implemented")
    }

    @SuppressLint("SetTextI18n")
    private fun personalInfo() {
        binding.apply {
            viewModel.userInfo.observe(viewLifecycleOwner, Observer {
                tvName.text = it.name
                tvPhone.text = "+${it.phone}"
                tvCarNumber.text = it.car_number
                tvEmail.text = it.email
            })
        }
    }
}