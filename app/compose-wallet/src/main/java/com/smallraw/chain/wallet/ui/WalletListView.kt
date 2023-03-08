package com.smallraw.chain.wallet.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import com.squareup.contour.ContourLayout

class WalletListView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ContourLayout(context, attrs) {
    private val chainList = CoinListView(context).apply {
//        layoutParams = LayoutParams(60.dip, LayoutParams.MATCH_PARENT)
    }
//
//    private val bio = TextView(context).apply {
//        textSize = 16f
//        text = "..."
//    }

    init {
        background = ColorDrawable(Color.RED)

        chainList.layoutBy(
            x = leftTo { parent.left() }.widthOf { 60.xdip },
            y = topTo { parent.top() }.bottomTo { parent.bottom() }
        )
    }
}