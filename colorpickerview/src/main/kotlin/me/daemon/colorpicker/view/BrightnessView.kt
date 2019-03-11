package me.daemon.colorpicker.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import me.daemon.colorpicker.internal.Callback
import me.daemon.colorpicker.internal.ColorPicker
import me.daemon.colorpicker.painter.BrightnessPainter

/**
 * @author daemon
 * @since 2019-02-22 14:40
 */
class BrightnessView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Callback {

    private lateinit var colorPicker: ColorPicker

    var brightness = 1f
        set(brightness) {
            val b = Math.max(0f, Math.min(1f, brightness))
            if (field != b) {
                field = b

                invalidate()

                colorPicker
                        .beginTransaction()
                        .brightness(b)
                        .commit(propagate = true, force = true)
            }
        }

    var brightnessPainter: BrightnessPainter? = null
        set(value) {
            field = value?.apply {
                updateByValue(
                        this@BrightnessView,
                        colorPicker.getBrightness()
                )
            }
        }

    private var isChanging = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        brightnessPainter?.onSizeChanged(
                this,
                w,
                h
        )

        brightnessPainter?.updateByValue(
                this,
                brightness
        )
    }

    override fun onDraw(canvas: Canvas) {
        brightnessPainter?.onDraw(
                this,
                canvas,
                colorPicker.getColor(),
                brightness,
                isChanging
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        brightnessPainter ?: return super.onTouchEvent(event)

        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            ACTION_DOWN -> {
                isChanging = true

                update(x, y, true)
                return true
            }

            ACTION_MOVE -> {
                update(x, y, true)
            }

            ACTION_UP -> {
                isChanging = false

                update(x, y, true)

                performClick()
            }

        }

        return super.onTouchEvent(event)
    }

    internal fun setColorPicker(colorPicker: ColorPicker) {
        this.colorPicker = colorPicker.apply {
            addCallback(this@BrightnessView)
        }
    }

    private fun update(
            x: Float,
            y: Float,
            propagate: Boolean
    ) {
        val painter = brightnessPainter ?: return

        val brightness = painter.onUpdate(this, x, y)

        colorPicker
                .beginTransaction()
                .brightness(brightness)
                .commit(propagate, force = true)

        invalidate()
    }

    override fun callback(
            hue: Float,
            saturation: Float,
            brightness: Float,
            alpha: Float
    ) {
    }

}