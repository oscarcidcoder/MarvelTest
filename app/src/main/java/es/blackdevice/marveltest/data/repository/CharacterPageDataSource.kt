package es.blackdevice.marveltest.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import es.blackdevice.marveltest.data.entity.Character
import es.blackdevice.marveltest.data.remote.NetworkDataSource
import java.net.HttpURLConnection

/**
 * Data source for pagination
 * Created by ocid on 10/28/21.
 */
class CharacterPageDataSource(private val remoteDataSource: NetworkDataSource) : PagingSource<Int, Character>() {

    private var currentPage = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val nextPage = params.key ?: currentPage
        return try {
            val response = remoteDataSource.getCharacters(nextPage)
            /*if (response.isSuccessful) {
                val body = response.body()

                val data = body?.data?.results.orEmpty()
                return LoadResult.Page(
                    data = data,
                    prevKey = if (nextPage == 0) null else nextPage - 1,
                    nextKey = if (data.isEmpty()) null else currentPage + 1
                ).also { currentPage++ }
            }*/
            val body = response.body()

            val data = body?.data?.results.orEmpty()
            LoadResult.Page(
                data = data,
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = if (data.isEmpty()) null else currentPage + 1
            ).also { currentPage++ }
        } catch (ex: Exception) {
            LoadResult.Error(throwable = ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>) = state.anchorPosition

}