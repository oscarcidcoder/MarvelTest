package es.blackdevice.marveltest.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import es.blackdevice.marveltest.domain.use_case.CharacterUseCases
import kotlinx.coroutines.flow.Flow
import es.blackdevice.marveltest.data.entity.Character
import es.blackdevice.marveltest.presentation.detail.FeedCharacterEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * Created by ocid on 10/28/21.
 */
class MainViewModel(private val characterUseCases: CharacterUseCases) : ViewModel() {

    private lateinit var _charactersFlow: Flow<PagingData<Character>>
    val getCharacters: Flow<PagingData<Character>>
        get() = _charactersFlow

    init {
        feedCharacters()
    }

    private fun feedCharacters() {
        _charactersFlow = characterUseCases.getCharactersUseCase().cachedIn(viewModelScope)
    }
}