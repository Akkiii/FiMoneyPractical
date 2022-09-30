package com.fimoney.practical.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.fimoney.practical.R
import com.fimoney.practical.databinding.DialogMovieDetailBinding
import com.fimoney.practical.model.response.MovieDetailResponse
import com.fimoney.practical.ui.base.BaseBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class MovieDetailDialog(private val movieDetailResponse: MovieDetailResponse) :
    BaseBottomSheetDialogFragment<DialogMovieDetailBinding>(R.layout.dialog_movie_detail) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_BottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            (dialog as BottomSheetDialog).apply {
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                behavior.isDraggable = true
                behavior.isHideable = true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding)
        {
            model = movieDetailResponse

            imageViewClose.setOnClickListener {
                dismiss()
            }

            textViewMovieDesc.setOnClickListener {
                textViewMovieDesc.toggle()
            }
        }
    }
}