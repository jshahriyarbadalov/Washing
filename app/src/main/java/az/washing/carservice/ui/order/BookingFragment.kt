package az.washing.carservice.ui.order

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import az.washing.carservice.databinding.FragmentBookingBinding
import az.washing.carservice.models.ReservationAdd
import az.washing.carservice.models.ReservationUpdate
import az.washing.carservice.models.Washing
import az.washing.carservice.utils.CarType
import az.washing.carservice.utils.Constants.Companion.STATUS_UPDATE
import az.washing.carservice.utils.Constants.Companion.WASHING_ID
import az.washing.carservice.utils.ServiceType
import java.text.SimpleDateFormat
import java.util.*


class BookingFragment : Fragment(), BookingView, OnItemSelectedListener {

    companion object {
        const val DATE_FORMAT = "dd.MM.yyyy"
        const val LIST_INDEX_ZERO = 0
    }

    private val viewModel by viewModels<OrderViewModel>()

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    private var washings: ArrayList<Washing> = arrayListOf()
    private var washingNames: ArrayList<String> = arrayListOf()
    private var timeList: ArrayList<String> = arrayListOf()
    private var carType: String = ""
    private var serviceType: String = ""
    private var getTime = ""
    private var getDay = ""
    private var status = 0
    private var washingId: Int? = 0
    private val cal = Calendar.getInstance()
    private val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.view = this
        chooseCarType()
        chooseServices()
        dateTimePicker()
        getEditOrder()

        viewModel.washings.observe(viewLifecycleOwner, Observer {
            washings.addAll(it)
            initSpinner()
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            with(binding) {
                pbLoader.isVisible = it
                tvLoaderTxt.isVisible = it
                viewTransparent.isVisible = it
            }
        })
        viewModel.bookedOrders.observe(viewLifecycleOwner, Observer {
            timeList.clear()
            timeList.addAll(it)
            initSpinnerTime()
        })

        binding.apply {
            isClickable(true)
            pbLoader.isVisible = false
            viewTransparent.isVisible = false
            tvLoaderTxt.isVisible = false
            tvShowTime.isVisible = false
            spinTime.isVisible = false
            btnConfirm.isClickable = false

            spinWashingName.onItemSelectedListener = this@BookingFragment
            btnConfirm.setOnClickListener {
                onOrderClick()
            }

            cbCancel.setOnClickListener {
                status = if (cbCancel.isChecked) {
                    1
                } else {
                    0
                }
            }

        }
    }

    private fun onOrderClick() = with(binding) {
        Log.d("Hello", "$carType, $serviceType, $getDay, $getTime")
        if (carType.isNotEmpty() && serviceType.isNotEmpty()
            && getDay.isNotEmpty() && getTime.isNotEmpty()
        ) {
            pbLoader.isVisible = true
            tvLoaderTxt.isVisible = true
            viewTransparent.isVisible = true
            isClickable(false)

            if (arguments?.getString(STATUS_UPDATE).equals("updated")) {
                viewModel.updateReservation(
                    ReservationUpdate(
                        washingId,
                        carType,
                        serviceType,
                        getDay,
                        getTime,
                        status
                    )
                )
            } else {
                viewModel.addReservation(
                    ReservationAdd(
                        washingId,
                        carType,
                        serviceType,
                        getDay,
                        getTime
                    )
                )
            }

        } else {
            Toast.makeText(
                root.context,
                getString(az.washing.carservice.R.string.true_order_fill),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getEditOrder() = with(binding) {
        val vehicle = arguments?.getString("vehicle_type")
        val service = arguments?.getString("service_type")
        val orderDay = arguments?.getString("order_day")
        val orderTime = arguments?.getString("order_time")

        if (!vehicle.isNullOrEmpty() && !service.isNullOrEmpty()
            && !orderDay.isNullOrEmpty() && !orderTime.isNullOrEmpty()
        ) {
            btnConfirm.text = getString(az.washing.carservice.R.string.save)
            cbCancel.isVisible = true
            tvShowTimeLbl.isVisible = true
            tvCurrentTime.isVisible = true
            if (vehicle == CarType.JEEP) {
                rbJeep.isChecked = true
                carType = CarType.JEEP
            } else {
                rbSedan.isChecked = true
                carType = CarType.SEDAN
            }

            when (service) {
                ServiceType.FULL -> {
                    rbFull.isChecked = true
                    serviceType = ServiceType.FULL
                }
                ServiceType.YARIM -> {
                    rbHalf.isChecked = true
                    serviceType = ServiceType.YARIM
                }
                else -> {
                    rbChemical.isChecked = true
                    serviceType = ServiceType.XIMCISTKA
                }
            }
            getDay = orderDay
            tvShowDate.text = getDay
            getTime = orderTime
            tvShowTimeLbl.text = getTime
        }

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        washings.find { it.washingName == washingNames[p2] }.let {
            washingId = it?.id
        }
        if (washingId != 0) {
            washingId?.let { viewModel.getTimesData(it, sdf.format(cal.time)) }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) = Unit


    private fun initSpinner() {
        filter()
        val spinAdapter: ArrayAdapter<String>? = context?.let {
            ArrayAdapter(
                it,
                R.layout.simple_spinner_item,
                washingNames
            )
        }
        spinAdapter?.setDropDownViewResource(
            R.layout.simple_spinner_dropdown_item
        )

        binding.apply {
            spinWashingName.adapter = spinAdapter

            val washOrderId = arguments?.getString("washing_name")
            val washId = arguments?.getInt(WASHING_ID)

            if (arguments?.getString(STATUS_UPDATE).equals("updated")) {
                washOrderId?.let { orderElement ->

                    washings.find { it.washingName == orderElement }.let {
                        washingId = it?.id
                        val getIndex = washings.indexOf(it)
                        if (getIndex <= spinWashingName.size) {
                            spinWashingName.setSelection(LIST_INDEX_ZERO)
                        } else {
                            spinWashingName.setSelection(getIndex)
                        }
                    }
                    washingId?.let { viewModel.getTimesData(it, getDay) }
                }
            } else {
                washId?.let { indexId ->
                    if (indexId <= spinWashingName.size) {
                        spinWashingName.setSelection(LIST_INDEX_ZERO)
                    } else {
                        spinWashingName.setSelection(indexId)
                    }
                    washings.find { it.washingName == spinWashingName.selectedItem }.let {
                        washingId = it?.id
                    }
                }
            }
        }
    }

    private fun initSpinnerTime() = with(binding) {

        val spinTimeAdapter: ArrayAdapter<String>? = context?.let {
            ArrayAdapter(
                it,
                R.layout.simple_spinner_item,
                timeList
            )
        }
        spinTimeAdapter?.setDropDownViewResource(
            R.layout.simple_spinner_dropdown_item
        )
        spinTime.adapter = spinTimeAdapter

        spinTime.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                timeList[selectedItemPosition]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        val orderTime = arguments?.getString("order_time")
        if (arguments?.getString(STATUS_UPDATE).equals("updated")) {
            getTime = orderTime.toString()
            timeList.find { it == orderTime }.let {
                val getTimeIndex = timeList.indexOf(it)
                if (getTimeIndex <= spinTime.size) {
                    spinTime.setSelection(LIST_INDEX_ZERO)
                } else {
                    spinTime.setSelection(getTimeIndex)
                }
            }
        }
    }

    private fun filter() {
        for (item in washings) {
            item.washingName?.let { washingNames.add(it) }
        }
    }

    override fun filterForTime() {
        binding.apply {
            spinTime.isVisible = true
            tvShowTime.isVisible = true
            btnConfirm.isCheckable = true
        }

    }


    private fun chooseCarType() {
        binding.apply {
            rbSedan.setOnClickListener {
                if (rbSedan.isChecked) {
                    carType = CarType.SEDAN
                }
            }
            rbJeep.setOnClickListener {
                if (rbJeep.isChecked) {
                    carType = CarType.JEEP
                }
            }
        }
    }

    private fun chooseServices() {
        binding.apply {
            rbFull.setOnClickListener {
                if (rbFull.isChecked) {
                    serviceType = ServiceType.FULL
                }
            }
            rbHalf.setOnClickListener {
                if (rbHalf.isChecked) {
                    serviceType = ServiceType.YARIM
                }
            }
            rbChemical.setOnClickListener {
                if (rbChemical.isChecked) {
                    serviceType = ServiceType.XIMCISTKA
                }
            }
        }
    }

    private fun dateTimePicker() {

        binding.apply {

            val datePicker =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val currentDay = sdf.format(cal.time)
                    val chooseDay = sdf.format(Calendar.getInstance().time)

                    if (cal.time.after(Calendar.getInstance().time) || currentDay.equals(
                            chooseDay
                        )
                    ) {
                        tvShowDate.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                az.washing.carservice.R.color.light_gray
                            )
                        )
                        tvShowDate.text = sdf.format(cal.time)
                        washingId?.let { viewModel.getTimesData(it, sdf.format(cal.time)) }
                        getDay = tvShowDate.text.toString()

                    } else {
                        Toast.makeText(context, "Günü düz seçin", Toast.LENGTH_LONG).show()
                        tvShowDate.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                az.washing.carservice.R.color.red
                            )
                        )
                        tvShowDate.text = sdf.format(cal.time)
                    }
                }

            btnShowDate.setOnClickListener {
                DatePickerDialog(
                    root.context, datePicker,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }


    private fun isClickable(isClick: Boolean) = with(binding) {
        btnConfirm.isCheckable = isClick
        btnShowDate.isCheckable = isClick
        rbChemical.isClickable = isClick
        rbHalf.isClickable = isClick
        rbFull.isClickable = isClick
        rbJeep.isClickable = isClick
        rbSedan.isClickable = isClick
        spinWashingName.isClickable = isClick
        spinTime.isClickable = isClick
        cbCancel.isClickable = isClick
    }

    override fun showSuccessMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        isClickable(true)
        binding.apply {
            pbLoader.isVisible = false
            tvLoaderTxt.isVisible = false
            viewTransparent.isVisible = false
        }
    }

    override fun goBack() {
        binding.pbLoader.isVisible = false
        findNavController().navigate(az.washing.carservice.R.id.action_bookingFragment_to_mainFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

