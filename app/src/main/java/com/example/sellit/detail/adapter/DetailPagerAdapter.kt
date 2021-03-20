package com.example.sellit.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sellit.databinding.ItemDetailBinding
import com.example.sellit.imagecache.ImageLoader

private class ClassifiedItemDiffCallback : DiffUtil.ItemCallback<androidx.core.util.Pair<String,String>>() {
    override fun areItemsTheSame(oldItem: androidx.core.util.Pair<String,String>, newItem: androidx.core.util.Pair<String,String>): Boolean {
        return oldItem.first == newItem.first && oldItem.second == newItem.second
    }

    override fun areContentsTheSame(oldItem: androidx.core.util.Pair<String,String>, newItem: androidx.core.util.Pair<String,String>): Boolean {
        return oldItem == newItem
    }

}

class DetailPagerAdapter :
    ListAdapter<androidx.core.util.Pair<String,String>, DetailPagerAdapter.ClassifiedViewHolder>(ClassifiedItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassifiedViewHolder =
        ClassifiedViewHolder(
            ItemDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ClassifiedViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    public override fun getItem(position: Int): androidx.core.util.Pair<String,String> {
        return super.getItem(position)
    }

    class ClassifiedViewHolder(val binding: ItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: androidx.core.util.Pair<String,String>) {
            binding.progressbar.isVisible = true

            ImageLoader.getInstance().loadImage(item.second!!,item.first!!,binding.imageView){
                binding.progressbar.isVisible = false

            }

        }
    }
}