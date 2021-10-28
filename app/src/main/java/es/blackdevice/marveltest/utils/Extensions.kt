package es.blackdevice.marveltest.utils

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewAnimationUtils
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.TintAwareDrawable
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

internal fun Activity.attachFragment(manager: FragmentManager, containerId: Int, view: Fragment, tag: String) {
    manager.beginTransaction()
        .replace(containerId, view, tag)
        .commitNowAllowingStateLoss()
}

inline fun <reified T> Result<T>.doIfFailure(callback: (error: String?, throwable: Throwable?) -> Unit): Result<T> {
    if (this is Result.Error) {
        callback(message, throwable)
    }
    return this
}

inline fun <reified T> Result<T>.doIfSuccess(callback: (value: T) -> Unit): Result<T> {
    if (this is Result.Success) {
        callback(value)
    }
    return this
}

inline fun <reified T, reified R> Result<T>.convertIfSuccess(convert: (value: T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> {
            Result.build { convert(value) }
        }
        is Result.Error -> Result.Error(message,throwable)
    }
}

// ----------- Handler LifeCycle Extensions ----------- //

inline fun<reified MODEL : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> MODEL): MODEL {
    val viewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = factory() as T
    }

    return ViewModelProvider(this, viewModelFactory)[MODEL::class.java]
}

inline fun<reified MODEL : ViewModel> Fragment.getViewModel(crossinline factory: () -> MODEL): MODEL {
    val viewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = factory() as T
    }

    return ViewModelProvider(this, viewModelFactory)[MODEL::class.java]
}

//LIVEDATA
/**
 * Extension para subscribir el observer de [LiveData]
 */
fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}

fun <T> MutableLiveData<T>.modifyValue(transform: T.() -> T) {
    this.value = this.value?.run(transform)
}

fun <T> MutableLiveData<T>.mutation(actions: MutableLiveData<T>.() -> Unit) {
    actions()
    this.value = this.value
}

// Handler Request Permissions
/**
 * Metodo que valida si se tiene consedido el permiso solicitado
 * @param permission {@link android.Manifest.permission}
 * @return true, si el permiso fue concedido
 */
fun Context.hasPermission(@NonNull permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED


// Handler images

fun ImageView.loadUrl(url: String, width: Int = this.width, height: Int = this.height) {
    Glide.with(context).load(url).placeholder(android.R.drawable.ic_menu_report_image)
            .diskCacheStrategy(DiskCacheStrategy.DATA).priority(Priority.IMMEDIATE)
            .dontAnimate().override(width,height).into(this)
}

/**
 * Metodo extendido del ImageView para realizar el cambio de color a la imagen que se
 * muestra dentro del widget
 *
 * @param colorRes color que se quiere tintar
 */
fun ImageView.tintSrc(@ColorRes colorRes: Int) {
    val drawable = DrawableCompat.wrap(drawable)
    DrawableCompat.setTint(drawable, ContextCompat.getColor(context, colorRes))
    setImageDrawable(drawable)
    if (drawable is TintAwareDrawable) invalidate()
}

fun Collection<String>.bulletList(): String {
    return "\n" + this.joinToString(separator = "\n") {
        HtmlCompat.fromHtml("&#8226 $it",HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

// ------- > Handler VIEWS

fun View.isVisibile(): Boolean = this.visibility == View.VISIBLE

fun View.isGone(): Boolean = this.visibility == View.GONE

fun View.isInvisible(): Boolean = this.visibility == View.INVISIBLE

fun View.toggleVisibility(animate: Boolean = true) {
    if (isVisibile())
        gone(animate)
    else
        visible(animate)
}

//region manejo de la Visibilidad

fun View.gone(animate: Boolean = true) {
    hide(View.GONE, animate)
}

fun View.invisible(animate: Boolean = true) {
    hide(View.INVISIBLE, animate)
}

fun View.visible(animate: Boolean = true) {
    if (animate) {
        animate().alpha(1f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                visibility = View.VISIBLE
            }
        })
    } else
        visibility = View.VISIBLE
}

private fun View.hide(hidingStrategy: Int, animate: Boolean = true) {
    if (animate) {
        animate().alpha(0f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = hidingStrategy
            }
        })
    } else
        visibility = hidingStrategy
}

enum class Orientation { CENTER, LEFT, RIGHT, TOP, BOTTOM }
fun View.circularReveal(
    orientation: List<Orientation> = listOf(Orientation.CENTER),
    duration: Long = 400
) {

    val x = when {
        orientation.contains(Orientation.LEFT) -> 0
        orientation.contains(Orientation.CENTER) -> right / 2
        else -> right
    }

    val y = when {
        orientation.contains(Orientation.TOP) -> 0
        orientation.contains(Orientation.CENTER) -> bottom / 2
        else -> bottom
    }

    val startRadius = 0
    val endRadius = Math.hypot(
        width.toDouble(),
        height.toDouble()
    ).toInt()

    val anim = ViewAnimationUtils.createCircularReveal(
        this,
        x,
        y,
        startRadius.toFloat(),
        endRadius.toFloat()
    )
    anim.duration = duration
    anim.start()
}


//endregion


/**
 * Sets text and content description using same string
 */
fun TextView.setTextWithContentDescription(value : String?) {
    text = value
    contentDescription = value
}

/**
 * Button enabling/disabling modifiers
 */

fun Button.disableButton() {
    isEnabled = false
    alpha = 0.3f
}

fun Button.enableButton() {
    isEnabled = true
    alpha = 1.0f
}

/**
 * Sets color to status bar
 */
fun Window.addStatusBarColor(@ColorRes color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        this.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        this.statusBarColor = ContextCompat.getColor(this.context, color)
    }
}

fun Boolean.asInt(): Int {
    return if (this) 1 else 0
}

fun Int.asBoolean(): Boolean {
    return (this == 1)
}

fun Boolean.asByte(): Byte {
    return if (this) 1 else 0
}

fun Byte.asBoolean(): Boolean {
    return (this == 1.toByte())
}

/**
 * Realizar una funcion con un delay
 */
fun withDelay(delay : Long, block : () -> Unit) {
    Handler().postDelayed(Runnable(block), delay)
}

fun Activity.screenWidth(): Int {
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}

fun Activity.screenHeight(): Int {
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.heightPixels
}

inline fun <T:Any> whenNullCallback(input: T?, callback: () -> T): T{
    return input ?: callback()
}

fun Context.color(@ColorRes resId: Int): Int =
    ContextCompat.getColor(this, resId)

/**
 * Convert dp integer to pixel
 */
fun Context.dpToPx(dp : Int): Float {
    val displayMetrics = this.resources.displayMetrics
    return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).toFloat()
}

/**
 * Computes status bar height
 */
fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen",
        "android")
    if (resourceId > 0) {
        result = this.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

/**
 * Metodo para mostrar u ocultar el teclado
 *
 * @param show true para mostrar el teclado, false lo opuesto
 */
fun View.showKeyboard(show: Boolean = true) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (show) {
        if (requestFocus()) imm.showSoftInput(this, 0)
    } else {
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}



@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@IntRange(from = 0, to = 3)
fun Context.getConnectionType(): Int {
    var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    result = 2
                } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    result = 1
                } else if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)){
                    result = 3
                }
            }
        }
    } else {
        cm?.run {
            cm.activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    result = 2
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    result = 1
                } else if(type == ConnectivityManager.TYPE_VPN) {
                    result = 3
                }
            }
        }
    }
    return result
}

/**
 * Execute block of code if network is available
 */
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
inline fun Context.withNetwork(block: () -> Unit): Boolean {
    return try {
        if (getConnectionType() != 0) {
            block()
            return true
        }
        false
    } catch (e: Exception) {
        false
    }
}

//region Manejo de DATA con GSON

inline fun <reified T : Any> String.jsonToObject(): T {
    val gson = GsonBuilder().serializeNulls().setLenient().setPrettyPrinting().create()
    val type = object : TypeToken<T>() {}.type
    return gson.fromJson(this, type)
}

inline fun <reified T : Any> Gson.jsonToObject(json: String): T {
    val type = object : TypeToken<T>() {}.type
    return fromJson(json, type)
}

inline fun <reified T : Any> T.objectToJson(): String {
    val type = object : TypeToken<T>() {}.type
    return GsonBuilder().setLenient().setPrettyPrinting().create().toJson(this,type)
}

//endregion

fun ByteArray.fromByteArrayToString() = String(this,Charsets.UTF_8)

//region SHARED_PREFERENCES

inline fun <reified T : Any> SharedPreferences.getObject(key: String): T? {
    return getString(key,null)?.jsonToObject<T>()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T {
    return when (T::class) {
        Boolean::class -> getBoolean(key, defaultValue as? Boolean? ?: false) as T
        Float::class -> getFloat(key, defaultValue as? Float? ?: 0.0f) as T
        Int::class -> getInt(key, defaultValue as? Int? ?: 0) as T
        Long::class -> getLong(key, defaultValue as? Long? ?: 0L) as T
        String::class -> getString(key, defaultValue as? String? ?: "") as T
        else -> {
            if (defaultValue is Set<*>) {
                getStringSet(key, defaultValue as Set<String>) as T
            } else {
                val typeName = T::class.java.simpleName
                throw Error("Unable to get shared preference with value type '$typeName'. Use getObject")
            }
        }
    }
}

// Ejemplo myAppPreferences["someKey"] = 20
@Suppress("UNCHECKED_CAST")
inline operator fun <reified T : Any> SharedPreferences.set(key: String, value: T) {
    with(edit()) {
        when (T::class) {
            Boolean::class -> putBoolean(key, value as Boolean)
            Float::class -> putFloat(key, value as Float)
            Int::class -> putInt(key, value as Int)
            Long::class -> putLong(key, value as Long)
            String::class -> putString(key, value as String)
            else -> {
                if (value is Set<*>) {
                    putStringSet(key, value as Set<String>)
                } else {
                    val json = value.objectToJson()
                    putString(key, json)
                }
            }
        }
        apply()
    }
}

//endregion

