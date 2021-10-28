package es.blackdevice.marveltest.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import es.blackdevice.marveltest.utils.Result
import es.blackdevice.marveltest.data.entity.Character

/**
 * Created by ocid on 10/28/21.
 */
interface CharacterRepository {

    fun getCharacters(offset: Int = 20): Flow<PagingData<Character>>

    suspend fun getCharacter(characterID: Int): Result<Character>

}