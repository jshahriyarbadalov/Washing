package az.washing.carservice.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import az.washing.carservice.R
import az.washing.carservice.databinding.FragmentPersonalBinding
import az.washing.carservice.models.ProfileUpdate
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
                isVisibleEditText(false)
                isVisibleText(true)
            }
        })
        personalInfo()
        binding.btnExit.setOnClickListener { viewModel.logout() }
        binding.btnSave.setOnClickListener { profileUpdate() }
    }

    override fun goLogout() {
        findNavController().clearBackStack(R.id.personalFragment)
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    override fun goBooking(id: Int) {
        TODO("Not yet implemented")
    }

    override fun goMap(address: String) {
        TODO("Not yet implemented")
    }

    override fun showUpdateMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("SetTextI18n")
    private fun personalInfo() {
        binding.apply {
            viewModel.userInfo.observe(viewLifecycleOwner, Observer {
                tvName.text = it.name
                tvPhone.text = "+${it.phone}"
                tvCarNumber.text = it.car_number
                tvEmail.text = it.email

                etName.setText(it.name)
                etCarNumber.setText(it.car_number)
                etEmail.setText(it.email)

                etName.text = etName.text
                etCarNumber.text = etCarNumber.text
                etEmail.text = etEmail.text
            })

            ivEdit.setOnClickListener {
                isVisibleText(false)
                isVisibleEditText(true)
            }
        }
    }

    private fun profileUpdate() = with(binding) {
        val name = etName.text.toString()
        val carNumber = etCarNumber.text.toString()
        val email = etEmail.text.toString()
        viewModel.profileUpdate(ProfileUpdate(name, carNumber, email))
    }

    private fun isVisibleText(visible: Boolean) = with(binding) {
        tvName.isVisible = visible
        tvCarNumber.isVisible = visible
        tvEmail.isVisible = visible
        btnExit.isVisible = visible
    }

    private fun isVisibleEditText(visible: Boolean) = with(binding) {
        etName.isVisible = visible
        etCarNumber.isVisible = visible
        etEmail.isVisible = visible
        btnSave.isVisible = visible
    }
}