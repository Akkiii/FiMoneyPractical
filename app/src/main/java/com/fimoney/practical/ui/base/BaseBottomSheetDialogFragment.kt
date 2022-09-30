package com.fimoney.practical.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.fimoney.practical.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseBottomSheetDialogFragment<out Binding : ViewBinding>(
    @LayoutRes private val contentLayoutId: Int
) :
    BottomSheetDialogFragment() {

    private var _binding: Binding? = null
    val binding: Binding
        get() = _binding ?: throw IllegalStateException(
            "BottomSheetDialogFragment $this binding cannot be accessed before onCreateView() or " +
                    "after onDestroyView() is called."
        )

    private var _context: Context? = null

    protected open fun applyOverrideTheme(): Int {
        return R.style.Theme_FiMoneyDemo
    }

    protected open fun applyOverrideLocal(): Locale? {
        return null
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        return super.onGetLayoutInflater(savedInstanceState).cloneInContext(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_FiMoney_BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val locale = applyOverrideLocal()
        val view = inflater.cloneInContext(
            requireContext()
        ).inflate(contentLayoutId, container, false)
        if (locale != null) {
            ViewCompat.setLayoutDirection(
                view,
                TextUtilsCompat.getLayoutDirectionFromLocale(locale)
            )
        }

        _binding = DataBindingUtil.bind(view)
        return binding.root
    }

    override fun getContext(): Context {
        return _context ?: run {
            val baseCtx = checkNotNull(super.getContext()) {
                "Fragment $this not attached to a context."
            }
            val locale = applyOverrideLocal()
            ContextThemeWrapper(baseCtx, applyOverrideTheme()).apply {

            }.also {
                _context = it
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
