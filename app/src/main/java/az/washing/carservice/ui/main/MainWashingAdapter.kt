package az.washing.carservice.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import az.washing.carservice.databinding.ItemWashingBinding
import az.washing.carservice.models.Washing
import com.bumptech.glide.Glide


class MainWashingAdapter(var view: MainView) :
    RecyclerView.Adapter<MainWashingAdapter.MainWashingVH>() {

    private var _binding: ItemWashingBinding? = null
    private val binding get() = _binding!!
    private var washings: ArrayList<Washing> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainWashingVH {
        _binding = ItemWashingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainWashingVH(binding)
    }

    override fun onBindViewHolder(holder: MainWashingVH, position: Int) {
        holder.bind(washings[position], position)
    }

    override fun getItemCount(): Int {
        return washings.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(washingList: List<Washing>) {
        washings.clear()
        washings.addAll(washingList)
        notifyDataSetChanged()
    }

    inner class MainWashingVH(binding: ItemWashingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(washing: Washing, position: Int) {
            binding.apply {
                tvTitle.text = washing.washingName
                tvName.text = washing.ownerName
                tvAddress.text = washing.address
                if (!washing.image.isNullOrEmpty()) {
                    Glide.with(binding.root).load(washing.image).into(ivIcon)
                }

//                ivAdd.setOnClickListener {
//                    view.goBooking(position)
//                }
                ivMap.setOnClickListener {
                    washing.address?.let { it1 -> view.goMap(it1) }
                }
            }
        }
    }
}

//https://stackoverflow.com/questions/22704451/open-google-maps-through-intent-for-specific-location-in-android