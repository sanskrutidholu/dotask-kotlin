package com.example.dotaskapp

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ListView

//  code for custom list view with no scrolling functionality
class NoScrollListView : ListView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure (widthMeasureSpec : Int, heightMeasureSpec: Int) {
        val heightMeasureSpec_custom : Int = MeasureSpec.makeMeasureSpec(
                Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST)

        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom)

        val params : ViewGroup.LayoutParams = layoutParams
        params.height = measuredHeight

    }
}