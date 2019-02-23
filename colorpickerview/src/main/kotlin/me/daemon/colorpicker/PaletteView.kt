package me.daemon.colorpicker

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import me.daemon.colorpicker.internal.ColorPicker

/**
 * @author daemon
 * @since 2019-02-23 18:57
 */
class PaletteView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    internal lateinit var colorPicker: ColorPicker

    init {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }

}