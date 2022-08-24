package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr)   {
    private var widthSize = 0
    private var heightSize = 0
    private var enabledBackground: Drawable? = null
    private var disabledBackground: Drawable? = null
    private var textColor: Int = 0
    private var text: String = "Download"
    private var textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var radioButton: RadioGroup


    private val valueAnimator = ValueAnimator()


    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->

    }



    init {
        textColor = ContextCompat.getColor(context, android.R.color.background_light)
        textPaint.color = textColor
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.round_corners)
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.round_corners_colured)
        isClickable = true
        background = disabledBackground
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            //  text= getString(R.styleable.LoadingButton_text).toString()
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            var  mShadow =  Paint(Paint.ANTI_ALIAS_FLAG)
            mShadow.isDither = true;
            mShadow.shader = Shader()

            canvas.drawRect(0f, 0f, getWidth().toFloat(), getHeight().toFloat(), mShadow);
            textPaint.textSize = 85f
            canvas.drawText(text, 320f, 105f, textPaint)


        }


    }

    override fun performClick(): Boolean {
        super.performClick()


        if (background.equals(enabledBackground)) {
            background = disabledBackground
            textColor = Color.WHITE
            textPaint.color = textColor
            text = "Download"


        } else {
            background = enabledBackground
            text = "Loading ..."

            textColor = Color.RED
            textPaint.color = textColor

        }

        Handler().postDelayed({
            val animator = ObjectAnimator.ofFloat(this, View.ROTATION, -360f, 0f)
            // val animator2 = ObjectAnimator.of(this.textPaint, View.ALPHA, -360f, 0f)

            animator.duration = 1000
            animator.start()
            if (background.equals(enabledBackground)) {
                background = disabledBackground
                textColor = Color.WHITE
                textPaint.color = textColor
                text = "Download"


            } else {
                background = enabledBackground
                text = "Loading ..."

                textColor = Color.RED
                textPaint.color = textColor

            }
        }, 1700)

        invalidate()
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }




}