package com.example.lightningnews.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lightningnews.R
import com.example.lightningnews.data.model.Article
import com.example.lightningnews.databinding.RowNewsBinding
import java.text.SimpleDateFormat

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    var clicked = false
    private val callback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = RowNewsBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class NewsViewHolder(
            val binding: RowNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(article: Article) {
            with(binding) {

                tvTitle.text = article.title

                tvDescription.text = article.description

                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val output = article.publishedAt?.let { parser.parse(it)?.let { formatter.format(it) } }

                tvPublishedAt.text = output

                tvSource.text = article.source?.name
            }
            Glide.with(binding.ivArticleImage.context)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.loading_bg)
                    .into(binding.ivArticleImage)
            binding.ivFavoriteItem.setOnClickListener {

                clicked = true
                if (clicked) {
                    onItemClickListener?.let {
                        binding.ivFavoriteItem.setImageResource(R.drawable.fovorite_bg)
                        it(article)
                    }
                } else {
                    clicked = false
                }
            }
            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(article)

                }
            }


        }

    }

    private var onItemClickListener: ((Article) -> Unit)? = null
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }


}