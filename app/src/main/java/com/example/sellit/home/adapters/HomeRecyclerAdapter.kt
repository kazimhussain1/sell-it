package com.example.sellit.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sellit.databinding.ItemClassifiedBinding
import com.example.sellit.home.api.response.ClassifiedItem
import com.example.sellit.imagecache.ImageLoader

private class ImageItemDiffCallback : DiffUtil.ItemCallback<ClassifiedItem>() {
    override fun areItemsTheSame(oldItem: ClassifiedItem, newItem: ClassifiedItem): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: ClassifiedItem, newItem: ClassifiedItem): Boolean {
        return oldItem.price == newItem.price
                && oldItem.name == newItem.name
                && oldItem.createdAt == newItem.createdAt
    }

}

class HomeRecyclerAdapter :
    ListAdapter<ClassifiedItem, HomeRecyclerAdapter.ImageViewHolder>(ImageItemDiffCallback()) {

    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(
            ItemClassifiedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

//    public override fun getItem(position: Int): ImageItem {
//        return super.getItem(position)
//    }

    inner class ImageViewHolder(val binding: ItemClassifiedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageView.setOnClickListener {
                listener?.onItemClick(adapterPosition, binding)
            }
        }

        fun bind(item: ClassifiedItem) {

            ImageLoader.getInstance().loadImage(item.imageUrls[0],item.imageIds[0], binding.imageView)
            binding.name.text = item.name
            binding.price.text = item.price

            binding.price.forceLayout()
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int, binding: ItemClassifiedBinding)
    }
}