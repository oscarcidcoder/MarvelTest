package es.blackdevice.marveltest.data.remote

import es.blackdevice.marveltest.data.entity.MarvelWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by ocid on 10/27/21.
 */
interface CharacterApi {

    @GET("/characters")
    suspend fun getCharacters(
          @Query("limit") limit: Int = 20,
          @Query("offset") offset: Int = 0
    ): Response<MarvelWrapper>

    @GET("/characters/{characterId}")
    suspend fun getCharacter(@Path("characterId") characterId: String): Response<MarvelWrapper>

}