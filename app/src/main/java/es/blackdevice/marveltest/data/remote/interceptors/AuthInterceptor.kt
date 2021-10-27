package es.blackdevice.marveltest.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by ocid on 10/27/21.
 */
class AuthInterceptor(
    private val privateKey: String,
    private val publicKey: String
) : Interceptor {

    private val TS_KEY = "ts"
    private val APIKEY_KEY = "apikey"
    private val HASH_KEY = "hash"

    @Throws(NoSuchAlgorithmException::class)
    fun calculateMD5(ts: String): String{
        try {
            val msgToEncode = ts+privateKey+publicKey
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(msgToEncode.toByteArray(Charsets.UTF_8)))
                .toString(16)
        } catch (ex: NoSuchAlgorithmException) {
            throw ex
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val ts = System.currentTimeMillis().toString()

        val hashResult = calculateMD5(ts)

        val urlAuth = chain.request().url.newBuilder().apply {
            addQueryParameter(TS_KEY, ts)
            addQueryParameter(APIKEY_KEY, publicKey)
            addQueryParameter(HASH_KEY, hashResult)
        }.build()

        val requestResult = chain.request().newBuilder().url(urlAuth).build()
        return chain.proceed(requestResult)
    }
}