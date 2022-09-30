package com.fimoney.practical.extension

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.core.graphics.ColorUtils
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fimoney.practical.R
import com.fimoney.practical.utils.Constant
import com.fimoney.practical.utils.DebounceTextWatcher
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.FormBody

fun Context.getThemeColorAttribute(@AttrRes resId: Int): Int {
    val typedValue = TypedValue()
    val typedArray = obtainStyledAttributes(typedValue.data, intArrayOf(resId))
    val color = typedArray.getColor(0, 0)
    typedArray.recycle()
    return color
}

val Context.windowBackgroundColor: Int
    get() = getThemeColorAttribute(android.R.attr.windowBackground)

val Context.colorSurface: Int
    get() = getThemeColorAttribute(com.google.android.material.R.attr.colorSurface)

fun Int.isDarkColor() = ColorUtils.calculateLuminance(this) < 0.35

inline fun <reified T : ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
    block()
    executePendingBindings()
}

fun FormBody.params(): Map<String, String> = buildMap {
    for (i in 0 until this@params.size) {
        put(name(i), value(i))
    }
}

fun Map<String, String>.toFromBody(): FormBody {
    val formBodyBuilder = FormBody.Builder()
    forEach {
        formBodyBuilder.add(it.key, it.value)
    }
    return formBodyBuilder.build()
}

inline val Fragment.viewLifecycleScope: LifecycleCoroutineScope
    get() = viewLifecycleOwner.lifecycleScope

inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

fun <E> MutableCollection<E>.clearAndAddAll(elements: Collection<E>.() -> Collection<E>) {
    clear()
    addAll(elements())
}

fun RecyclerView.scrollToEnd() {
    val position = adapter?.itemCount?.minus(1) ?: -1
    if (position >= 0) {
        val scroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        scroller.targetPosition = position
        requireLayoutManager().startSmoothScroll(scroller)
    }
}

fun RecyclerView.requireLayoutManager(): RecyclerView.LayoutManager {
    return checkNotNull(layoutManager) { "LayoutManager not attached to $this" }
}

fun EditText.setDebounce(DELAY: Long = 1200, f: () -> Unit) {
    this.addTextChangedListener(object : DebounceTextWatcher(DELAY) {
        override fun onDone() {
            f()
        }
    })
}

fun Fragment?.runOnUiThread(action: () -> Unit) {
    this ?: return
    if (!isAdded) return // Fragment not attached to an Activity
    activity?.runOnUiThread(action)
}

fun View.dismissKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    requireContext().toast(message, duration)
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun View.setOnClickListenerPushDown(
    scale: Float = Constant.PUSH_DOWN_SCALE,
    block: (View) -> Unit
) {
    PushDownAnim.setPushDownAnimTo(this)
        .setScale(PushDownAnim.MODE_SCALE, scale)
        .setDurationPush(Constant.PUSH_DOWN_PUSH_DURATION)
        .setDurationRelease(Constant.PUSH_DOWN_RELEASE_DURATION)
        .setOnClickListener {
            block(this)
        }
}




