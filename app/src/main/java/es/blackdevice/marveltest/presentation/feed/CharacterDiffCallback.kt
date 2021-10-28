package es.blackdevice.marveltest.presentation.feed

import androidx.recyclerview.widget.DiffUtil
import es.blackdevice.marveltest.data.entity.Character

/**
 * Handler differences in character list
 * Created by ocid on 10/28/21.
 */
class CharacterDiffCallback : DiffUtil.ItemCallback<Character>(){
    override fun areItemsTheSame(oldItem: Character, newItem: Character) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Character, newItem: Character) = oldItem == newItem
}