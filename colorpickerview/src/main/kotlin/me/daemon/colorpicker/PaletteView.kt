package me.daemon.colorpicker

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import me.daemon.colorpicker.internal.Callback
import me.daemon.colorpicker.internal.ColorPicker
import me.daemon.colorpicker.painter.PalettePainter1

/**
 * @author daemon
 * @since 2019-02-23 18:57
 */
class PaletteView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Callback {

    private var colorPicker: ColorPicker = ColorPicker(this)

    private var palettePainter: PalettePainter1? = null

    private var isChanging = false

    private var updated = true

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
                colorPicker.getColor(),
                isChanging
        ) ?: super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        palettePainter ?: return super.onTouchEvent(event)

        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                parent?.requestDisallowInterceptTouchEvent(true) //TODO to be removed

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

                parent?.requestDisallowInterceptTouchEvent(false) //TODO to be removed
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

    private fun update(
            x: Float,
            y: Float,
            propagate: Boolean
    ) {
        val painter = palettePainter ?: return

        updated = false
        painter.onUpdate(this, x, y, propagate)
        if (!updated) {
            throw IllegalStateException("PaletteView{$this}: ${painter.javaClass.name}#onUpdate" +
                    " did not update hue and saturation by calling" +
                    " PaletteView#updateHueAndSaturation(Float, Float, Boolean)")
        }
    }

    fun updateHueAndSaturation(
            hue: Float,
            saturation: Float,
            propagate: Boolean
    ) {
        updated = true
        val colorPicker = this.colorPicker
        colorPicker
                .beginTransaction()
                .hue(hue)
                .saturation(saturation)
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
    }

    /**
     * TODO for develop
     */
    fun subscribe(observer: ColorObserver) {
        colorPicker.subscribe(observer)
    }

}