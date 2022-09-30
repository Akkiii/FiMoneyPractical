package com.fimoney.practical.extension

import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fimoney.practical.R
import com.fimoney.practical.ui.home.adapter.CastCrewAdapter

@BindingAdapter("loadUrl")
fun ImageView.loadUrl(url: String?) {
    if (url != null) {
        load(url) {
            error(R.drawable.default_placeholder)
            crossfade(true)
        }
    } else {
        setImageResource(R.drawable.default_placeholder)
    }
}

@BindingAdapter("castCrew")
fun RecyclerView.castCrew(s: String?) {
    if (s.isNullOrBlank()) {
        isVisible = false
        return
    }
    isVisible = true
    val data = s.split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .takeIf { it.isNotEmpty() } ?: listOf(s)
    val amenitiesAdapter = (adapter as? CastCrewAdapter) ?: CastCrewAdapter().also {
        adapter = it
    }
    amenitiesAdapter.amenities = data
}

@BindingAdapter("ratingValue")
fun RatingBar.ratingValue(s: String?) {
    if (s.isNullOrBlank()) {
        rating = 0f
    } else {
        rating = if (s == "N/A") {
            0f
        } else {
            s.toFloat() / 2
        }
    }
}

