package es.blackdevice.marveltest.utils

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import es.blackdevice.marveltest.R
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Clases para el manejo de ViewBinding sin repeticion de codigo y usando mejor los recursos
 * Created by ocid on 1/5/21.
 */
abstract class BindingActivity<VB: ViewBinding> : AppCompatActivity() {

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater) -> VB

    protected var savedInstanceState: Bundle? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        this.savedInstanceState = savedInstanceState
        setupViews()
    }

    abstract fun setupViews()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

abstract class BindingFragment<VB: ViewBinding> : Fragment() {

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    abstract fun setupViews()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

// ---> USE <---
/*
class SomeFragment: Fragment(R.layout.some_fragment) {

  val binding by bindingLifecycleAware { SomeFragmentBinding.bind(requireView()) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.someTextView.text = "Hello View Binding!"
  }
}
*/

fun <T> Fragment.bindingLifecycleAware(initialise: () -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

        private var binding: T? = null

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
            this@bindingLifecycleAware
                .viewLifecycleOwner
                .lifecycle
                .removeObserver(this)
            super.onDestroy(owner)
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
            binding
                ?: initialise().also {
                    binding = it
                    this@bindingLifecycleAware
                        .viewLifecycleOwner
                        .lifecycle
                        .addObserver(this)
                }
    }