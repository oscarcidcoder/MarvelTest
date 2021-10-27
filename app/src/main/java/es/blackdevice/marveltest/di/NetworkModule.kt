package es.blackdevice.marveltest.di

import android.os.Build
import es.blackdevice.marveltest.data.remote.CharacterApi
import es.blackdevice.marveltest.data.remote.interceptors.AuthInterceptor
import es.blackdevice.marveltest.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT = 15L
private const val READ_TIMEOUT = 20L

/**
 * DI for Network resources
 *
 * Created by ocid on 10/27/21.
 */
val networkModule = module {

    single {
        OkHttpClient.Builder().apply {
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(AuthInterceptor(Constants.PRIVATE_KEY,Constants.PUBLIC_KEY))
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()
    }

    single {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(CharacterApi::class.java) }
}
