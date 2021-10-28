package es.blackdevice.marveltest.presentation.detail

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import es.blackdevice.marveltest.databinding.ActivityDetailBinding
import es.blackdevice.marveltest.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by ocid on 10/28/21.
 */
class DetailActivity : BindingActivity<ActivityDetailBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDetailBinding
        get() = ActivityDetailBinding::inflate

    private val characterId by lazy { intent.getIntExtra(CHARACTER_ID_PARAM, 0) }
    private val viewModel: DetailViewModel by viewModel { parametersOf(characterId) }

    override fun setupViews() {
        Log.i("DetailActivity", "setupViews: CharacterID -> $characterId")
        observe(viewModel.response) {
            when(it) {
                FeedCharacterEvent.Loading -> {

                }
                is FeedCharacterEvent.Error -> {

                }
                is FeedCharacterEvent.Success -> {
                    toggleVisibility()
                    with(binding) {
                        ivCharacter.loadUrl(it.character.thumbnail.fullPath)
                        tvName.text = it.character.name
                        tvDescription.text = it.character.description
                    }
                }
                null -> {}
            }
        }
    }

    private fun toggleVisibility() {
        with(binding) {
            ivCharacter.toggleVisibility()
            tvDescription.toggleVisibility()
            tvName.toggleVisibility()
            pbLoading.toggleVisibility()
        }
    }

    companion object {

        private const val CHARACTER_ID_PARAM = "es.blackdevice.marveltest_CHARACTER_ID"
        fun callActivity(context: Context, characterId: Int) {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(CHARACTER_ID_PARAM, characterId)
            }
            context.startActivity(intent)
        }
    }
}