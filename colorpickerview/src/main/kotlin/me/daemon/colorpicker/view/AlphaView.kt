package me.daemon.colorpicker.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import me.daemon.colorpicker.internal.Callback
import me.daemon.colorpicker.internal.ColorPicker
import me.daemon.colorpicker.painter.IAlphaPainter

/**
 * @author daemon
 * @since 2019-03-11 08:24
 */
class AlphaView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Callback, IView<AlphaView.AlphaValue> {

    private lateinit var colorPicker: ColorPicker

    var alphaPainter: IAlphaPainter? = null
        set(value) {
            field = value?.apply {
                updateByValue(
                        this@AlphaView
                )
            }
        }

    private var isChanging = false

    class AlphaValue : IView.Value() {

        var alpha: Float = 1f
            private set

        fun setValue(alpha: Float): AlphaValue {
            this.alpha = alpha
            set()
            return this
        }
    }

    private val alphaValue = AlphaValue()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        alphaPainter?.onSizeChanged(
                this,
                w,
                h
        )

        alphaPainter?.updateByValue(
                this
        )
    }

    override fun onDraw(canvas: Canvas) {
        alphaPainter?.onDraw(
                this,
                canvas,
                isChanging
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        alphaPainter ?: return super.onTouchEvent(event)

        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isChanging = true

                update(x, y, true)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                update(x, y, true)
            }

            MotionEvent.ACTION_UP -> {
                isChanging = false

                update(x, y, true)

                performClick()
            }

        }

        return super.onTouchEvent(event)
    }

    internal fun setColorPicker(colorPicker: ColorPicker) {
        this.colorPicker = colorPicker.apply {
            addCallback(this@AlphaView)
        }
    }

    private fun update(
            x: Float,
            y: Float,
            propagate: Boolean
    ) {
        val painter = alphaPainter ?: return

        alphaValue.reset()
        painter.onUpdate(this, x, y)

        if (!alphaValue.set) {
            throw java.lang.IllegalStateException("AlphaView{$this}: ${painter.javaClass.name}" +
                    "#onUpdate did not update alpha by calling" +
                    " AlphaValue#setValue(Float)"
            )
        }

        colorPicker
                .beginTransaction()
                .alpha(alphaValue.alpha)
                .commit(propagate, force = true)

        invalidate()
    }

    override fun callback(
            color: Int,
            hue: Float,
            saturation: Float,
            brightness: Float,
            alpha: Float
    ) {
        alphaPainter?.onColorChanged(this, color)
    }

    override fun getValue(): AlphaValue {
        return alphaValue
    }

    override fun getColor(): Int {
        return colorPicker.getColor()
    }

}