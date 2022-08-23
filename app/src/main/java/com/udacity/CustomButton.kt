package com.udacity

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat


class CustomButton : AppCompatButton, View.OnClickListener {
    private var enabledBackground: Drawable? = null
    private var disabledBackground: Drawable? = null
    private var textColorr: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        setTextColor(textColorr)
        textSize = 12f
        gravity = CENTER
        text = "Download"
        background =
            enabledBackground


    }

    private fun init() {
        textColorr = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.round_corners)
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.round_corners_colured)
    }

    override fun onClick(v: View?) {

    }

    override fun setOnClickListener(l: OnClickListener?) {

        background=disabledBackground
    }
}