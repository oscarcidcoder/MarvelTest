package es.blackdevice.marveltest.presentation.feed

import android.util.Log
import android.view.LayoutInflater
import es.blackdevice.marveltest.databinding.ActivityMainBinding
import es.blackdevice.marveltest.utils.BindingActivity
import es.blackdevice.marveltest.utils.observe
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BindingActivity<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    private val viewModel: MainViewModel by viewModel()

    override fun setupViews() {
        viewModel.getCharacters.observe(this, isLatest = true) {
            Log.d("MainActivity", "setupViews: $it")
        }
    }

}