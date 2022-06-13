package com.smallraw.chain.wallet.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.smallraw.chain.wallet.R
import com.squareup.contour.ContourLayout

@Suppress("unused")
class CoinListView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ContourLayout(context, attrs) {

    private val addCoin = ImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        load(android.R.drawable.ic_input_add)
    }
    private val division1 = View(context).apply {
        background = ColorDrawable(Color.parseColor("#ffffff"))
    }
    private val chainList = RecyclerView(context).apply {
        layoutManager = LinearLayoutManager(context)
    }
    private val division2 = View(context).apply {
        background = ColorDrawable(Color.parseColor("#ffffff"))
    }
    private val hint = ImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        load(R.drawable.vector_ic_select_coin_help)
    }

    private val selectCursor = ImageView(context).apply {
        load(R.drawable.ic_select_coin_cursor)
        visibility = View.VISIBLE
    }


    init {
        addCoin.layoutBy(
            x = leftTo { parent.centerX() - addCoin.measuredWidth / 2 }.widthOf { 36.xdip },
            y = topTo { parent.top() }.heightOf { 36.ydip }
        )
        division1.layoutBy(
            x = leftTo { parent.centerX() - division1.measuredWidth / 2 }.widthOf { 20.xdip },
            y = topTo { addCoin.bottom() + 7.ydip }.heightOf { 1.ydip }
        )
        chainList.layoutBy(
            x = leftTo { parent.left() }.rightTo { parent.right() },
            y = topTo { division1.bottom() + 7.ydip }
        )
        division2.layoutBy(
            x = leftTo { parent.centerX() - division2.measuredWidth / 2 }.widthOf { 20.xdip },
            y = topTo { chainList.bottom() + 7.ydip }.heightOf { 1.ydip }
        )
        hint.layoutBy(
            x = leftTo { parent.centerX() - hint.measuredWidth / 2 }.widthOf { 18.xdip },
            y = topTo { division2.bottom() + 9.ydip }.heightOf { 18.ydip }
        )

        selectCursor.layoutBy(
            x = leftTo { parent.left() }.widthOf { 5.xdip },
            y = topTo { parent.top() + 20.ydip }.heightOf { 25.ydip }
        )
    }
}