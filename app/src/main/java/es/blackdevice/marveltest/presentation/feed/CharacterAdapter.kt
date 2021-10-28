package es.blackdevice.marveltest.presentation.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import es.blackdevice.marveltest.R
import es.blackdevice.marveltest.data.entity.Character
import es.blackdevice.marveltest.databinding.ItemCharacterBinding
import es.blackdevice.marveltest.utils.loadUrl

/**
 * Class to show pagination character
 * Created by ocid on 10/28/21.
 */
class CharacterAdapter(private val onCharacterClick: (characterId: Int) -> Unit) :
    PagingDataAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.also {
            holder.bind(it,onCharacterClick)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_character,parent,false)
        )
    }

    inner class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemCharacterBinding.bind(itemView)

        fun bind(character: Character, listener: (Int) -> Unit) {
            with(binding) {
                tvName.text = character.name
                ivCharacter.loadUrl(character.thumbnail.fullPath)

                root.setOnClickListener { listener(character.id) }
            }
        }

    }
}