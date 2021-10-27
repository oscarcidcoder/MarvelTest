package es.blackdevice.marveltest.data.entity

/**
 * Created by ocid on 10/27/21.
 */
data class ContainerWrapper(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<Character>
)
