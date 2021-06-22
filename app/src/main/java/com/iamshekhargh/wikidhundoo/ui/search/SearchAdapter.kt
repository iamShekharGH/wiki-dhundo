package com.iamshekhargh.wikidhundoo.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iamshekhargh.wikidhundoo.R
import com.iamshekhargh.wikidhundoo.databinding.ItemSearchBinding
import com.iamshekhargh.wikidhundoo.network.response.Page

class SearchAdapter : ListAdapter<Page, SearchAdapter.SearchAdapterViewHolder>(DiffUtilsItem()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapterViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchAdapterViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(p: Page) {
            binding.apply {
                itemSearchTitle.text = p.title
                itemSearchDescription.text = p.pageimage
//                if (p.thumbnail.source.isNotEmpty())
                val t = p.thumbnail
                var imgUrl = ""
                if (t != null) {
                    imgUrl = t.source
                }
                Glide.with(itemSearchImageview).load(imgUrl).centerCrop()
                    .placeholder(R.drawable.image)
                    .into(itemSearchImageview)
            }
        }
    }
}

class DiffUtilsItem : DiffUtil.ItemCallback<Page>() {
    override fun areItemsTheSame(oldItem: Page, newItem: Page): Boolean =
        oldItem.pageid == newItem.pageid

    override fun areContentsTheSame(oldItem: Page, newItem: Page): Boolean = oldItem == newItem

}