package es.blackdevice.marveltest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import es.blackdevice.marveltest.data.entity.Character
import es.blackdevice.marveltest.data.remote.NetworkDataSource
import es.blackdevice.marveltest.domain.repository.CharacterRepository
import es.blackdevice.marveltest.utils.Result
import es.blackdevice.marveltest.utils.convertIfSuccess
import kotlinx.coroutines.flow.Flow

/**
 * Repository implementation
 * Created by ocid on 10/28/21.
 */
class CharacterRepositoryImpl(private val remoteDataSource: NetworkDataSource) : CharacterRepository {

    override fun getCharacters(offset: Int): Flow<PagingData<Character>> = Pager(
        config = PagingConfig(pageSize = offset, prefetchDistance = 4),
        pagingSourceFactory = { CharacterPageDataSource(remoteDataSource) }
    ).flow

    override suspend fun getCharacter(characterID: Int): Result<Character> {
        return remoteDataSource.getCharacter(characterID).convertIfSuccess {
            it.first()
        }
    }


}