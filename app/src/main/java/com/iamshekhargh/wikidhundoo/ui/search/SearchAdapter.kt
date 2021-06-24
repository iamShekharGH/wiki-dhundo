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
import java.util.*

class SearchAdapter constructor(
    private val listener: OnPageClicked
) :
    ListAdapter<Page, SearchAdapter.SearchAdapterViewHolder>(DiffUtilsItem()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapterViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchAdapterViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: SearchAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchAdapterViewHolder(
        private val binding: ItemSearchBinding,
        private val listener: OnPageClicked
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(p: Page) {
            binding.apply {

                root.setOnClickListener {
                    listener.pageItemClicked(p)
                }
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
                if (p.terms.description != null) {
                    val text = p.terms.description.toString().replace("[", "").replace("]", "")
                    val textUpper = text.substring(0, 1).uppercase(Locale.getDefault()) + text.substring(1)
                    itemSearchDescription.text = textUpper
                }
            }
        }
    }

    public interface OnPageClicked {
        fun pageItemClicked(p: Page)
    }
}

class DiffUtilsItem : DiffUtil.ItemCallback<Page>() {
    override fun areItemsTheSame(oldItem: Page, newItem: Page): Boolean =
        oldItem.pageid == newItem.pageid

    override fun areContentsTheSame(oldItem: Page, newItem: Page): Boolean = oldItem == newItem
}
