package me.daemon.colorpicker

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import me.daemon.colorpicker.internal.ColorPicker
import me.daemon.colorpicker.painter.BrightnessPainter

/**
 * @author daemon
 * @since 2019-02-22 14:40
 */
class BrightnessView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    internal lateinit var colorPicker: ColorPicker

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

    private var brightnessPainter: BrightnessPainter? = null

    override fun onDraw(canvas: Canvas) {
        brightnessPainter?.drawBrightness(
                this,
                canvas,
                brightness
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        val brightnessPainter = this.brightnessPainter ?: return super.onTouchEvent(event)

        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            ACTION_DOWN -> {
                brightnessPainter.update(this, x, y)
                return true
            }

            ACTION_MOVE -> {
                brightnessPainter.update(this, x, y)
            }

            ACTION_UP -> {
                brightnessPainter.update(this, x, y)

                performClick()
            }

        }

        return super.onTouchEvent(event)
    }

}