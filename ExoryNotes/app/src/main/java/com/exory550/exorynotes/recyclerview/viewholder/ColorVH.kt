package com.exory550.exorynotes.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.exory550.exorynotes.databinding.RecyclerColorBinding
import com.exory550.exorynotes.miscellaneous.Operations
import com.exory550.exorynotes.recyclerview.ItemListener
import com.exory550.exorynotes.room.Color

class ColorVH(private val binding: RecyclerColorBinding, listener: ItemListener) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            listener.onClick(adapterPosition)
        }
    }

    fun bind(color: Color) {
        val value = Operations.extractColor(color, binding.root.context)
        binding.root.setCardBackgroundColor(value)
    }
}
