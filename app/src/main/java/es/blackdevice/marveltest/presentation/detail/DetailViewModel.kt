package es.blackdevice.marveltest.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import es.blackdevice.marveltest.data.entity.Character
import es.blackdevice.marveltest.domain.use_case.CharacterUseCases
import es.blackdevice.marveltest.utils.doIfFailure
import es.blackdevice.marveltest.utils.doIfSuccess
import kotlinx.coroutines.Dispatchers

/**
 * Created by ocid on 10/28/21.
 */
class DetailViewModel(private val characterId: Int, private val characterUseCases: CharacterUseCases) : ViewModel() {

    private val _characterInfo = MutableLiveData<FeedCharacterEvent>()
    val characterInfo: LiveData<FeedCharacterEvent>
        get() = _characterInfo

    val response = liveData<FeedCharacterEvent>(Dispatchers.IO) {
        emit(FeedCharacterEvent.Loading)
        characterUseCases.getCharacterUseCase(characterId).doIfSuccess {
            emit(FeedCharacterEvent.Success(it))
        }.doIfFailure { error, throwable ->
            emit(FeedCharacterEvent.Error(error ?: "Unknown"))
        }
    }

    /*
    init {
        liveData<FeedCharacterEvent>(Dispatchers.IO) {
            emit(FeedCharacterEvent.Loading)
            characterUseCases.getCharacterUseCase(characterId).doIfSuccess {
                emit(FeedCharacterEvent.Success(it))
            }.doIfFailure { error, throwable ->
                emit(FeedCharacterEvent.Error(error ?: "Unknown"))
            }
        }
    } */

}

/**
 * Events handle character status load
 */
sealed class FeedCharacterEvent {
    class Success(val character: Character): FeedCharacterEvent()
    class Error(val errorText: String): FeedCharacterEvent()
    object Loading : FeedCharacterEvent()
}