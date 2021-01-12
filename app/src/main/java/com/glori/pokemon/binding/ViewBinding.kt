package com.glori.pokemon.binding

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.glori.pokemon.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("paletteImage", "paletteCard")
fun bindLoadImagePalette(view: AppCompatImageView, url: String, paletteCard: MaterialCardView) {
    Glide.with(view.context)
        .load(url)
        .listener(
            GlidePalette.with(url)
                .use(BitmapPalette.Profile.MUTED_LIGHT)
                .intoCallBack { palette ->
                    val rgb = palette?.dominantSwatch?.rgb
                    if (rgb != null) {
                        paletteCard.setCardBackgroundColor(rgb)
                    }
                }.crossfade(true)
        )
        .apply(
            RequestOptions().placeholder(R.drawable.animation_loading)
                .error(R.drawable.ic_broken_image)
        )
        .into(view)
}


@BindingAdapter("paletteImageUrl", "paletteShapeableImageView")
fun bindLoadImagePalette(view: AppCompatImageView, url: String, paletteCard: ShapeableImageView) {
    Glide.with(view.context)
        .load(url)
        .listener(
            GlidePalette.with(url)
                .use(BitmapPalette.Profile.MUTED_LIGHT)
                .intoCallBack { palette ->
                    val rgb = palette?.dominantSwatch?.rgb
                    if (rgb != null) {
                        paletteCard.setBackgroundColor(rgb)
                    }
                }.crossfade(true)
        )
        .apply(
            RequestOptions().placeholder(R.drawable.animation_loading)
                .error(R.drawable.ic_broken_image)
        )
        .into(view)
}
