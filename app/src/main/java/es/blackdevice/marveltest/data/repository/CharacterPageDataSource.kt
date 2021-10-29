package es.blackdevice.marveltest.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import es.blackdevice.marveltest.data.entity.Character
import es.blackdevice.marveltest.data.remote.NetworkDataSource

/**
 * Data source for pagination
 * Created by ocid on 10/28/21.
 */
class CharacterPageDataSource(private val remoteDataSource: NetworkDataSource) : PagingSource<Int, Character>() {

    private var currentPage = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val position = params.key ?: currentPage
        val offset = if (params.key != null) (position  * params.loadSize) else currentPage

        return try {
            val response = remoteDataSource.getCharacters(offset)
            if (response.isSuccessful) {
                val body = response.body()

                val data = body?.data?.results.orEmpty()
                LoadResult.Page(
                    data = data,
                    prevKey = null,
                    nextKey = if (data.isEmpty()) null else currentPage + 1
                ).also { currentPage++ }
            } else {
                LoadResult.Error(Throwable(response.message()))
            }
        } catch (ex: Exception) {
            LoadResult.Error(throwable = ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>) = state.anchorPosition

}