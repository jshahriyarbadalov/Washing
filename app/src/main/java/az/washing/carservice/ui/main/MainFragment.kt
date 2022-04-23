package az.washing.carservice.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import az.washing.carservice.R
import az.washing.carservice.databinding.FragmentMainBinding
import az.washing.carservice.utils.Constants.Companion.WASHING_ID


class MainFragment : Fragment(), MainView {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val bundle = Bundle()

    private var adapter: MainWashingAdapter = MainWashingAdapter(this)

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.view = this
        binding.apply {
            rvWashings.adapter = adapter
            viewModel.isLoading.observe(viewLifecycleOwner, Observer {
                pbLoader.isVisible = it
                tvLoaderText.isVisible = it
            })
            viewModel.washing.observe(viewLifecycleOwner, Observer {
                adapter.setData(it)
            })
        }
    }

    override fun goBooking(id: Int) {
        bundle.putInt(WASHING_ID, id)
        findNavController().navigate(R.id.action_mainFragment_to_bookingFragment, bundle)
    }

    override fun goLogout() {
        TODO("Not yet implemented")
    }

    override fun goMap(address: String) {
        val url = "http://maps.google.com/maps?daddr=$address"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}