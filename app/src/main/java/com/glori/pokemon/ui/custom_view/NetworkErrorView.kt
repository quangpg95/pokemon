package com.glori.pokemon.ui.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.glori.pokemon.R

class NetworkErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var view: View

    val tvMessage: TextView by lazy {
        view.findViewById(R.id.tvErrorMessage)
    }

    private val btnRetry: Button by lazy {
        view.findViewById(R.id.btnRetry)
    }

    var onRetryListener: OnRetryListener? = null

    init {
        view = inflate(context, R.layout.layout_error_network_view, this)
        btnRetry.setOnClickListener {
            onRetryListener?.onRetry()
        }
    }


}

interface OnRetryListener {
    fun onRetry()
}
