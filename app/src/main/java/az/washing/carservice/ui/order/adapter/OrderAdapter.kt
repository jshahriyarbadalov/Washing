package az.washing.carservice.ui.order.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.ArraySet
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import az.washing.carservice.databinding.ItemOrderBinding
import az.washing.carservice.models.Order
import az.washing.carservice.ui.order.OrderView

class OrderAdapter(var view: OrderView) : RecyclerView.Adapter<OrderAdapter.OrderVH>() {
    private var _binding: ItemOrderBinding? = null
    private val binding get() = _binding!!
    private var orderList: ArrayList<Order> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderVH {
        _binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderVH(binding)
    }

    override fun onBindViewHolder(holder: OrderVH, position: Int) {
        holder.bind(orderList[position], position)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addOrderItems(orders: ArraySet<Order>) {
        orderList.clear()
        orderList.addAll(orders)
        notifyDataSetChanged()
    }

    inner class OrderVH(binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order, position: Int) {
            binding.apply {
                tvTitleWashing.text = order.washingName
                tvCarType.text = order.vehicle_type
                tvServiceType.text = order.service_type
                tvDayTime.text = "${order.day}, ${order.time}"

                ivEdit.setOnClickListener { view.editOrder(position) }
                ivDelete.isVisible = true
                ivDelete.setOnClickListener {
                    view.cancelOrder(position)
                }
            }
        }
    }
}