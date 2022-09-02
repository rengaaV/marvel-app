package com.vagner.marvel.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vagner.marvel.R
import com.vagner.marvel.data.model.character.CharacterModel
import com.vagner.marvel.databinding.ItemCharacterBinding

class CharacterAdapter(private val onClickItem: (CharacterModel) -> Unit) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<CharacterModel>() {
        override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name &&
                    oldItem.description == newItem.description &&
                    oldItem.thumbnail.path == newItem.thumbnail.path &&
                    oldItem.thumbnail.extension == newItem.thumbnail.extension
        }

        override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    var listCharacter: List<CharacterModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = listCharacter.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(listCharacter[position], onClickItem)
    }

    fun getCharacterPosition(position: Int): CharacterModel {
        return listCharacter[position]

    }

    inner class CharacterViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: CharacterModel, onClickItem: (CharacterModel) -> Unit) {
            binding.apply {
                tvNameCharacter.text = character.name

                if (character.description == "") {
                    tvDescriptionCharacter.text =
                        itemView.context.getString(R.string.text_description_empty)
                } else {
                    tvDescriptionCharacter.text = character.description
                }
                Glide.with(imgCharacter)
                    .load(character.thumbnail.path + "." + character.thumbnail.extension)
                    .into(imgCharacter)
            }
            itemView.setOnClickListener {
                onClickItem(character)
            }

        }

    }
}