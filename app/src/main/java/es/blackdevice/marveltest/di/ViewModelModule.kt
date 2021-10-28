package es.blackdevice.marveltest.di

import es.blackdevice.marveltest.domain.use_case.CharacterUseCases
import es.blackdevice.marveltest.domain.use_case.GetCharacterUseCase
import es.blackdevice.marveltest.domain.use_case.GetCharactersUseCase
import es.blackdevice.marveltest.presentation.detail.DetailViewModel
import es.blackdevice.marveltest.presentation.feed.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by ocid on 10/28/21.
 */
val viewModelModule = module {

    single { CharacterUseCases(
        getCharactersUseCase = GetCharactersUseCase(get()),
        getCharacterUseCase = GetCharacterUseCase(get())
    ) }

    viewModel { MainViewModel( characterUseCases = get() ) }
    viewModel { (characterId: Int) -> DetailViewModel(characterId, get()) }

}