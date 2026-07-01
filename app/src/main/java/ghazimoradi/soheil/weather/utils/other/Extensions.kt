package ghazimoradi.soheil.weather.utils.other

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.request.error
import com.google.android.material.snackbar.Snackbar
import ghazimoradi.soheil.weather.R.drawable.placeholder
import ghazimoradi.soheil.weather.utils.time.TimeUtils
import kotlinx.coroutines.launch

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ImageView.loadImage(url: String) {
    this.load(url) {
        crossfade(true)
        crossfade(500)
        diskCachePolicy(CachePolicy.ENABLED)
        error(placeholder)
    }
}

fun RecyclerView.setupRecyclerview(
    myLayoutManager: RecyclerView.LayoutManager,
    myAdapter: RecyclerView.Adapter<*>
) {
    this.apply {
        layoutManager = myLayoutManager
        setHasFixedSize(true)
        adapter = myAdapter
    }
}

fun String.convertToDayName(): String {
    val dateSplit = this.split("-")
    val timeUtils = TimeUtils(dateSplit[0].toInt(), dateSplit[1].toInt(), dateSplit[2].toInt())
    return timeUtils.weekDayStr
}

fun View.isVisible(isShownLoading: Boolean, container: View) {
    if (isShownLoading) {
        this.isVisible = true
        container.isVisible = false
    } else {
        this.isVisible = false
        container.isVisible = true
    }
}

fun Fragment.doWorkOnLifecycle(work: suspend () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(CREATED) {
            work()
        }
    }
}

fun ImageView.setTint(@ColorRes color: Int) {
    setColorFilter(context.getColor(color))
}