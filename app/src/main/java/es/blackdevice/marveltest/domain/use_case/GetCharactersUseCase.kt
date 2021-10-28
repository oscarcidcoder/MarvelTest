package es.blackdevice.marveltest.domain.use_case

import es.blackdevice.marveltest.domain.repository.CharacterRepository

/**
 * Created by ocid on 10/28/21.
 */
class GetCharactersUseCase(private val repository: CharacterRepository) {

    operator fun invoke() = repository.getCharacters()

}