package com.glori.pokemon.binding

import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.paging.LoadState

@BindingAdapter("error")
fun bindErrorMessage(textView: TextView, loadState: LoadState) {
    textView.apply {
        isVisible = loadState !is LoadState.Loading
        if (loadState is LoadState.Error) {
            text = loadState.error.localizedMessage
        }
    }
}

@BindingAdapter("loading")
fun bindLoading(progressBar: ProgressBar, loadState: LoadState) {
    progressBar.isVisible = loadState is LoadState.Loading
}

@BindingAdapter("retry")
fun bindRetry(textView: TextView, loadState: LoadState) {
    textView.apply {
        isVisible = loadState !is LoadState.Loading
    }
}

