package az.washing.carservice.ui.order

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import az.washing.carservice.databinding.FragmentBookingBinding
import az.washing.carservice.models.ReservationAdd
import az.washing.carservice.models.Washing
import az.washing.carservice.utils.CarType
import az.washing.carservice.utils.Constants.Companion.WASHING_ID
import az.washing.carservice.utils.ServiceType
import java.text.SimpleDateFormat
import java.util.*


class BookingFragment : Fragment(), BookingView, OnItemSelectedListener {

    companion object {
        const val DATE_FORMAT = "dd.MM.yyyy"
        const val TIME_FORMAT = "HH:mm:ss"
    }

    private val viewModel by viewModels<OrderViewModel>()

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    private var washings: ArrayList<Washing> = arrayListOf()
    private var washingNames: ArrayList<String> = arrayListOf()
    private var carType: String = ""
    private var serviceType: String = ""
    private var getTime = ""
    private var getDay = ""
    private var washingId: Int? = 0
    private val cal = Calendar.getInstance()
    private val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    private val timeSdf = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())

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
//        getEditOrder()
        viewModel.washings.observe(viewLifecycleOwner, Observer {
            washings.addAll(it)
            initSpinner()
        })
        binding.apply {
            isClickable(true)
            pbLoader.isVisible = false
            viewTransparent.isVisible = false
            tvLoaderTxt.isVisible = false
            spinWashingName.onItemSelectedListener = this@BookingFragment
            btnConfirm.setOnClickListener {
                Log.d("Hello", "$carType, $serviceType, $getDay, $getTime")
                if (carType.isNotEmpty() && serviceType.isNotEmpty()
                    && getDay.isNotEmpty() && getTime.isNotEmpty()
                ) {
                    pbLoader.isVisible = true
                    tvLoaderTxt.isVisible = true
                    viewTransparent.isVisible = true
                    isClickable(false)
                    viewModel.addReservation(
                        ReservationAdd(
                            washingId,
                            carType,
                            serviceType,
                            getDay,
                            getTime
                        )
                    )


                } else {
                    Toast.makeText(
                        root.context,
                        "Zəhmət olmasa sifarişi düzgün doldurun.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    private fun getEditOrder() {
        arguments?.getString("washing_name")
        arguments?.getString("vehicle_type")
        arguments?.getString("service_type")
        arguments?.getString("order_day")
        arguments?.getString("order_time")
        arguments?.getInt("order_cancel", 1)

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        washings.find { it.washingName == washingNames[p2] }.let {
            washingId = it?.id
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

        binding.spinWashingName.adapter = spinAdapter
        val washId = arguments?.getString(WASHING_ID)?.toInt()
        washId?.let { binding.spinWashingName.setSelection(it) }
    }

    private fun filter() {
        for (item in washings) {
            item.washingName?.let { washingNames.add(it) }
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
                if (rbSedan.isChecked) {
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
            val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                tvShowDate.text = sdf.format(cal.time)
                getDay = tvShowDate.text.toString()
            }

            val timePicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                tvShowTime.text = timeSdf.format(cal.time)
                getTime = tvShowTime.text.toString()
            }

            btnShowDate.setOnClickListener {
                DatePickerDialog(
                    root.context, datePicker,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            btnShowTime.setOnClickListener {
                TimePickerDialog(
                    root.context,
                    timePicker,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            }
        }
    }

    private fun isClickable(isClick: Boolean) = with(binding) {
        btnConfirm.isCheckable = isClick
        btnShowTime.isCheckable = isClick
        btnShowDate.isCheckable = isClick
        rbChemical.isClickable = isClick
        rbHalf.isClickable = isClick
        rbFull.isClickable = isClick
        rbJeep.isClickable = isClick
        rbSedan.isClickable = isClick
        spinWashingName.isClickable = isClick
    }

    override fun showSuccessMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun goBack() {
        binding.pbLoader.isVisible = false
        findNavController().navigate(az.washing.carservice.R.id.action_bookingFragment_to_mainFragment)
    }

    override fun getTimeText(timeText: String) {
        getTime = timeText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

