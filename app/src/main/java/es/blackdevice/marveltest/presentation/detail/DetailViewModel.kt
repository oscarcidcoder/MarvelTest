package es.blackdevice.marveltest.presentation.detail

import androidx.lifecycle.ViewModel
import es.blackdevice.marveltest.domain.use_case.CharacterUseCases

/**
 * Created by ocid on 10/28/21.
 */
class DetailViewModel(private val characterId: Int, private val characterUseCases: CharacterUseCases) : ViewModel() {
}