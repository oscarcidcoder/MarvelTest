package es.blackdevice.marveltest.data.remote

import es.blackdevice.marveltest.utils.Result
import retrofit2.Response

/**
 * Resolve response data from Rest API
 *
 * Created by ocid on 10/27/21.
 */
abstract class BaseDataSource {

    protected suspend fun <T> resolveResponse(callback: suspend () -> Response<T>): Result<T> {
        try {
            val response = callback()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.Success(body)
            }
            return Result.Error(response.message(),null)
        } catch (ex: Exception) {
            return Result.Error(ex.message,ex)
        }
    }

}