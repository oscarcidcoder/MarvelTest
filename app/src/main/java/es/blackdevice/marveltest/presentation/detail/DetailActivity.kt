package es.blackdevice.marveltest.presentation.detail

import android.view.LayoutInflater
import es.blackdevice.marveltest.databinding.ActivityDetailBinding
import es.blackdevice.marveltest.utils.BindingActivity

/**
 * Created by ocid on 10/28/21.
 */
class DetailActivity : BindingActivity<ActivityDetailBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDetailBinding
        get() = ActivityDetailBinding::inflate

    override fun setupViews() {
        TODO("Not yet implemented")
    }
}