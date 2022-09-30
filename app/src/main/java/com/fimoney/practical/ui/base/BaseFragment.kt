package com.fimoney.practical.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.fimoney.practical.R
import com.fimoney.practical.extension.getThemeColorAttribute
import com.fimoney.practical.extension.isDarkColor
import com.fimoney.practical.extension.windowBackgroundColor
import com.fimoney.practical.ui.main.MainView

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseFragment<out Binding : ViewBinding>(@LayoutRes private val contentLayoutId: Int) :
    Fragment() {

    private var _binding: Binding? = null
    val binding: Binding
        get() = _binding ?: throw IllegalStateException(
            "Fragment $this binding cannot be accessed before onCreateView() or " +
                    "after onDestroyView() is called."
        )

    private var mainView: MainView? = null
    private var _context: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainView) {
            mainView = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainView = null
    }

    override fun getContext(): Context {
        return _context ?: run {
            val baseCtx = checkNotNull(super.getContext()) {
                "Fragment $this not attached to a context."
            }
            ContextThemeWrapper(baseCtx, applyOverrideTheme()).apply {
            }.also {
                _context = it
            }
        }
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        return super.onGetLayoutInflater(savedInstanceState).cloneInContext(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(contentLayoutId, container, false)

        if (view.background == null) {
            view.setBackgroundColor(requireContext().windowBackgroundColor)
        }
        _binding = DataBindingUtil.bind(view)
        return binding.root
    }

    protected open fun applyOverrideTheme(): Int {
        return R.style.Theme_FiMoneyDemo
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
//        val themeContext = view?.context ?: return

//        val systemBarColor = themeContext.getThemeColorAttribute(android.R.attr.windowBackground)
//        setSystemBarColor(systemBarColor)
    }

    protected fun showProgress() {
        mainView?.showProgress()
    }

    protected fun hideProgress() {
        mainView?.hideProgress()
    }

    protected fun setSystemBarColor(@ColorInt color: Int) {
        setStatusBarColor(color)
        setNavigationBarColor(color)
    }

    protected fun setStatusBarColor(@ColorInt color: Int) {
        val window = activity?.window ?: return
        window.statusBarColor = color
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
            !color.isDarkColor()
    }

    protected fun setNavigationBarColor(@ColorInt color: Int) {
        val window = activity?.window ?: return
        window.navigationBarColor = color
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars =
            !color.isDarkColor()
    }
}
