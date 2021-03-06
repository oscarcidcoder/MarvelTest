package es.blackdevice.marveltest.data.remote

import es.blackdevice.marveltest.utils.Result
import es.blackdevice.marveltest.utils.convertIfSuccess

/**
 * Created by ocid on 10/28/21.
 */
class NetworkDataSource(private val api: CharacterApi) : BaseDataSource() {

    suspend fun getCharacters(offset: Int) = api.getCharacters(offset = offset)

    suspend fun getCharacter(characterId: Int) = resolveResponse {
            api.getCharacter(characterId)
        }.convertIfSuccess {
            it.data.results
        }


}