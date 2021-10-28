package es.blackdevice.marveltest.domain.use_case

import es.blackdevice.marveltest.domain.repository.CharacterRepository

/**
 * Created by ocid on 10/28/21.
 */
class GetCharacterUseCase(private val repository: CharacterRepository) {

    suspend operator fun invoke(characterId: Int) = repository.getCharacter(characterId)

}