package es.blackdevice.marveltest.di

import es.blackdevice.marveltest.data.repository.CharacterRepositoryImpl
import es.blackdevice.marveltest.domain.repository.CharacterRepository
import org.koin.dsl.module

/**
 * Created by ocid on 10/28/21.
 */
val repositoryModule = module {

    single<CharacterRepository> { CharacterRepositoryImpl(remoteDataSource = get()) }

}