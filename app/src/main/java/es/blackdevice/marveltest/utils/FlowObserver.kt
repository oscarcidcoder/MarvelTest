package es.blackdevice.marveltest.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Clase para observar los flujos de datos de Kotlin FLOW usando el ciclo de vida de
 * Android, con ello nos aseguramos de desconectar los observadores y reconectarlos
 * dependiendo el estado de la Actividad.
 *
 * Created by ocid on 1/5/21.
 */

@PublishedApi
internal class ObserverImpl<T> (
        lifecycleOwner: LifecycleOwner,
        private val flow: Flow<T>,
        private val collector: suspend (T) -> Unit
) : DefaultLifecycleObserver {

    private var actualJob: Job? = null

    override fun onStart(owner: LifecycleOwner) {
        actualJob = owner.lifecycleScope.launch {
            flow.collect {
                collector(it)
            }
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        actualJob?.cancel()
        actualJob = null
    }


    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }


    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

}

// ---> USE <---
/*

class YourActivity : AppCompatActivity() {

    private val viewModel: YourViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel
            .locations
            .onEach { Flow Data}
            .observeIn(this)

        viewModel
            .users
            .observe(this) {
                Flow Data
            }
    }
}

*/

inline fun <reified T> Flow<T>.observe(
        lifecycleOwner: LifecycleOwner,
        noinline collector: suspend (T) -> Unit
) {
    ObserverImpl(lifecycleOwner, this, collector)
}

inline fun <reified T> Flow<T>.observeIn(
        lifecycleOwner: LifecycleOwner
) {
    ObserverImpl(lifecycleOwner, this, {})
}