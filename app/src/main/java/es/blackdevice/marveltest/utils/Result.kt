package es.blackdevice.marveltest.utils

/**
 * Handler responses utility
 *
 * Created by ocid on 1/5/21.
 */
sealed class Result<out T> {
    data class Success<T>(val value: T): Result<T>()
    data class Error(val message: String?, val throwable: Throwable?): Result<Nothing>()

    companion object Factory{
        inline fun <T> build(function: () -> T): Result<T> =
            try {
               Success(function.invoke())
            } catch (e: Exception) {
                Error(e.message,e)
            }
    }
}

/**
 * For empty result retrieve
 */
typealias SimpleResult = Result<Unit>

val Result<*>.isSuccess
    get() = this is Result.Success && value != null