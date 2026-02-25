package com.exory550.exorynotes.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.exory550.exorynotes.databinding.RecyclerLabelBinding
import com.exory550.exorynotes.recyclerview.ItemListener

class LabelVH(private val binding: RecyclerLabelBinding, listener: ItemListener) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            listener.onClick(adapterPosition)
        }

        binding.root.setOnLongClickListener {
            listener.onLongClick(adapterPosition)
            return@setOnLongClickListener true
        }
    }

    fun bind(value: String) {
        binding.root.text = value
    }
}
