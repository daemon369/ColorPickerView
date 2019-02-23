package me.daemon.colorpicker

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import me.daemon.colorpicker.internal.ColorPicker
import me.daemon.colorpicker.painter.PalettePainter1

/**
 * @author daemon
 * @since 2019-02-23 18:57
 */
class PaletteView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    internal var colorPicker: ColorPicker? = null

    private var palettePainter: PalettePainter1? = null

    private var isChanging = false

    init {
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        palettePainter?.onSizeChanged(
                this,
                w,
                h
        )
    }

    override fun onDraw(canvas: Canvas) {
        palettePainter?.onDraw(
                this,
                canvas,
                colorPicker?.getColor() ?: 0,
                isChanging
        ) ?: super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        val painter = palettePainter ?: return super.onTouchEvent(event)

        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isChanging = true

                painter.onUpdate(this, x, y)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                painter.onUpdate(this, x, y)
            }

            MotionEvent.ACTION_UP -> {
                isChanging = false

                painter.onUpdate(this, x, y)

                performClick()

            }

        }

        return super.onTouchEvent(event)
    }

    /**
     * set custom palette painter
     *
     * 设置自定义调色板绘制器
     *
     * @param palettePainter custom palette painter
     *                       调色板绘制器
     */
    fun setPalettePainter(palettePainter: PalettePainter1?) {
        this.palettePainter = palettePainter
    }

}