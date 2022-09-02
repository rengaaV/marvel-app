package com.vagner.marvel.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vagner.marvel.data.model.comic.ComicModel
import com.vagner.marvel.databinding.ItemComicBinding

class ComicAdapter() :
    RecyclerView.Adapter<ComicAdapter.ComicViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ComicModel>() {
        override fun areItemsTheSame(oldItem: ComicModel, newItem: ComicModel): Boolean {
            return oldItem.id == newItem.id && oldItem.description == newItem.description &&
                    oldItem.thumbnail.path == newItem.thumbnail.path &&
                    oldItem.thumbnail.extension == newItem.thumbnail.extension
        }

        override fun areContentsTheSame(oldItem: ComicModel, newItem: ComicModel): Boolean {
           return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    var listComic: List<ComicModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        return ComicViewHolder(
            ItemComicBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = listComic.size

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bind(listComic[position])
    }

    inner class ComicViewHolder(private val binding: ItemComicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comic: ComicModel) {
            binding.apply {
                tvNameComic.text = comic.title
                tvDescriptionComic.text = comic.description

                Glide.with(imgComic)
                    .load(comic.thumbnail.path + "." + comic.thumbnail.extension)
                    .into(imgComic)
            }

        }

    }
}