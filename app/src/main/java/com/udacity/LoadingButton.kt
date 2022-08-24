package com.udacity

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr)   {

    private var bgColor: Int = Color.BLACK
    private var textColor: Int = Color.BLACK // default color

    // tells the compiler that the value of a variable
    // must never be cached as its value may change outside
    @Volatile
    private var progress: Double = 0.0
    private lateinit var valueAnimator: ValueAnimator

    // observes the state of button
    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
    }

    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()
        invalidate()    // redraw the screen
        requestLayout() // when rectangular progress dimension changes
    }

    // call after downloading is completed
    fun hasCompletedDownload() {
        // cancel the animation when file is downloaded
        valueAnimator.cancel()
        buttonState = ButtonState.Completed
        invalidate()
        requestLayout()
    }

    private var widthSize = 0
    private var heightSize = 0
    private var enabledBackground: Drawable? = null
    private var disabledBackground: Drawable? = null

    private var text: String = "Download"
    private var textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var radioButton: RadioGroup






    init {
        isClickable = true
        valueAnimator = AnimatorInflater.loadAnimator(
            context,
            // properties for downloading progress is defined
            R.animator.anim
        ) as ValueAnimator

        valueAnimator.addUpdateListener(updateListener)

        // initialize custom attributes of the button
        val attr = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0,
            0
        )
        try {

            // button back-ground color
            bgColor = attr.getColor(
                R.styleable.LoadingButton_buttonBackground,
                ContextCompat.getColor(context, R.color.colorPrimary)
            )

            // button text color
            textColor = attr.getColor(
                R.styleable.LoadingButton_textColor,
                ContextCompat.getColor(context, R.color.white)
            )
        } finally {
            // clearing all the data associated with attribute
            attr.recycle()
        }
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
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER // button text alignment
        textSize = 55.0f //button text size
        typeface = Typeface.create("", Typeface.BOLD) // button text's font style
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.strokeWidth = 0f
        paint.color = bgColor
        // draw custom button
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        if (buttonState == ButtonState.Loading) {
            paint.color = Color.parseColor("#004349")
            canvas.drawRect(
                0f, 0f,
                (width * (progress / 100)).toFloat(), height.toFloat(), paint
            )
        }
        // check the button state
        val buttonText = if (buttonState == ButtonState.Loading)
            "Loading "// We are loading as button text
        else resources.getString(R.string.download)// download as button text

        // write the text on custom button
        paint.color = textColor
        canvas.drawText(buttonText, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)

       /* canvas.apply {
            var  mShadow =  Paint(Paint.ANTI_ALIAS_FLAG)
            mShadow.isDither = true;
            mShadow.shader = Shader()

            canvas.drawRect(0f, 0f, getWidth().toFloat(), getHeight().toFloat(), mShadow);
            textPaint.textSize = 85f
            canvas.drawText(text, 320f, 105f, textPaint)


        }*/


    }

    override fun performClick(): Boolean {
        super.performClick()
        if (buttonState == ButtonState.Completed) buttonState = ButtonState.Loading
        animation()
        Handler().postDelayed({
                              buttonState= ButtonState.Completed
        }, 1700)

       /* if (background.equals(enabledBackground)) {
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
            //val animator = ObjectAnimator.ofFloat(this, View.ROTATION, -360f, 0f)
            // val animator2 = ObjectAnimator.of(this.textPaint, View.ALPHA, -360f, 0f)

           // animator.duration = 1000
          //  animator.start()
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

        invalidate()*/
        return true
    }
    private fun animation() {
        valueAnimator.start()
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