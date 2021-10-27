package es.blackdevice.marveltest.data.entity

/**
 * Created by ocid on 10/27/21.
 */
data class Character(
    val id: Int,
     val name: String,
     val description: String,
     val thumbnail: Thumbnail,
     val resourceURI: String,
     val urls: List<URL>
 )
