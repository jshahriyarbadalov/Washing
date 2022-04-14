package az.washing.carservice.ui.order

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
import az.washing.carservice.databinding.FragmentOrderBinding
import az.washing.carservice.models.Order
import az.washing.carservice.models.Reservation
import az.washing.carservice.models.Washing
import az.washing.carservice.ui.order.adapter.OrderAdapter


class OrderFragment : Fragment(), OrderView {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val washingList = mutableListOf<Washing>()
    private val reservationList = mutableListOf<Reservation>()
    var orderList: ArrayList<Order> = arrayListOf()
    private var adapter: OrderAdapter = OrderAdapter(this)
    private val viewModel by viewModels<OrderViewModel>()
    private val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.washings.observe(viewLifecycleOwner, Observer {
            washingList.addAll(it)
        })
        viewModel.reservations.observe(viewLifecycleOwner, Observer {
            reservationList.addAll(it)
            filter()
            initAdapter()
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.apply {
                pbLoader.isVisible = it
                tvLoaderText.isVisible = it
            }
        })
        binding.fabAdd.setOnClickListener { orderOnClick() }
    }

    private fun initAdapter() {
        binding.rvOrderList.adapter = adapter
        adapter.addOrderItems(orderList)
    }

    private fun filter() {
        for (item in reservationList) {
            washingList.find { it.id == item.washing_id }.let {
                orderList.add(
                    Order(
                        it?.washingName.toString(),
                        item.vehicle_type,
                        item.service_type,
                        item.day,
                        item.time,
                        item.cancel
                    )
                )
            }
        }
    }

    private fun orderOnClick() {
        findNavController().navigate(R.id.action_orderFragment_to_bookingFragment2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun editOrder(orderId: Int) {
        bundle.putString("washing_name", orderList[orderId].washingName)
        bundle.putString("vehicle_type", orderList[orderId].vehicle_type)
        bundle.putString("service_type", orderList[orderId].service_type)
        bundle.putString("order_day", orderList[orderId].day)
        bundle.putString("order_time", orderList[orderId].time)
        bundle.putInt("order_cancel", 1)
        findNavController().navigate(R.id.action_orderFragment_to_bookingFragment2, bundle)
    }

}