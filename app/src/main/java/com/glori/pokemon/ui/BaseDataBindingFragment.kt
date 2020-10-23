package com.glori.pokemon.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseDataBindingFragment : Fragment() {
    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): Lazy<T> = lazy {
        DataBindingUtil.inflate(inflater, resId, container, false)
    }

}