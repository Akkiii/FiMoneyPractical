package com.fimoney.practical.ui.base

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InputField<T>(
    private val initialValue: T? = null,
    private val errorMessage: String? = null,
    private val validator: (T?) -> Boolean
) {
    val flow = MutableStateFlow(initialValue)

    private val _error = MutableStateFlow<String?>(null)
    val errorText: StateFlow<String?>
        get() = _error

    @Suppress("UNCHECKED_CAST")
    fun isValid() = if (validator(flow.value)) {
        _error.value = null
        true
    } else {
        _error.value = errorMessage
        false
    }

    fun reset() {
        flow.value = initialValue
    }
}
