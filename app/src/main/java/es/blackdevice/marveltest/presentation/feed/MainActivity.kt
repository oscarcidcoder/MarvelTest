package es.blackdevice.marveltest.presentation.feed

import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.blackdevice.marveltest.databinding.ActivityMainBinding
import es.blackdevice.marveltest.presentation.detail.DetailActivity
import es.blackdevice.marveltest.utils.BindingActivity
import es.blackdevice.marveltest.utils.observe
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BindingActivity<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    private val viewModel: MainViewModel by viewModel()

    private val adapter: CharacterAdapter by lazy { CharacterAdapter { characterId ->
            Log.i("MAinActivity", "ID Character -> $characterId")
            DetailActivity.callActivity(this,characterId)
        }
    }

    override fun setupViews() {
        with(binding) {
            rvCharacter.layoutManager = GridLayoutManager(this@MainActivity,2, RecyclerView.VERTICAL,false)
            rvCharacter.adapter = adapter
        }

        viewModel.getCharacters.observe(this, isLatest = true) {
            adapter.submitData(it)
        }
    }

}