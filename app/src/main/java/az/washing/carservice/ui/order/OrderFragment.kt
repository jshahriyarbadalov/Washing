package az.washing.carservice.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.collection.ArraySet
import androidx.collection.arraySetOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import az.washing.carservice.R
import az.washing.carservice.databinding.FragmentOrderBinding
import az.washing.carservice.models.Order
import az.washing.carservice.models.Reservation
import az.washing.carservice.models.ReservationUpdate
import az.washing.carservice.models.Washing
import az.washing.carservice.ui.order.adapter.OrderAdapter
import az.washing.carservice.utils.Constants.Companion.STATUS_UPDATE


class OrderFragment : Fragment(), OrderView {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val washingList = mutableSetOf<Washing>()
    private val reservationList = mutableSetOf<Reservation>()
    var orderList: ArraySet<Order> = arraySetOf()
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
        viewModel.viewOrder = this
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
        var name = ""
        for (item in reservationList) {
            washingList.find { it.id == item.washing_id }.let {
                val result = it?.washingName.isNullOrEmpty()
                name = if (result) {
                    ""
                } else {
                    it?.washingName.toString()
                }
                orderList.add(
                    Order(
                        name,
                        item.vehicle_type,
                        item.service_type,
                        item.day,
                        item.time,
                        item.status
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

    override fun showSuccessMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun editOrder(orderId: Int) {
        bundle.putString("washing_name", orderList.elementAt(orderId).washingName)
        bundle.putString("vehicle_type", orderList.elementAt(orderId).vehicle_type)
        bundle.putString("service_type", orderList.elementAt(orderId).service_type)
        bundle.putString("order_day", orderList.elementAt(orderId).day)
        bundle.putString("order_time", orderList.elementAt(orderId).time)
        bundle.putString(STATUS_UPDATE, "updated")
        findNavController().navigate(R.id.action_orderFragment_to_bookingFragment2, bundle)
    }

}