package com.exory550.exorynotes.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.exory550.exorynotes.databinding.ErrorBinding
import com.exory550.exorynotes.image.ImageError

class ErrorVH(private val binding: ErrorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(error: ImageError) {
        binding.Name.text = error.name
        binding.Description.text = error.description
    }
}
