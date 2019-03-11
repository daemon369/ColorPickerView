package me.daemon.colorpicker.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import me.daemon.colorpicker.internal.Callback
import me.daemon.colorpicker.internal.ColorPicker
import me.daemon.colorpicker.painter.PalettePainter

/**
 * @author daemon
 * @since 2019-02-23 18:57
 */
class PaletteView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Callback, IView<PaletteView.PaletteValue> {

    private lateinit var colorPicker: ColorPicker

    var palettePainter: PalettePainter? = null
        /**
         * set custom palette painter
         *
         * 设置自定义调色板绘制器
         *
         * @param palettePainter custom palette painter
         *                       调色板绘制器
         */
        set(palettePainter) {
            field = palettePainter?.apply {
                updateByValue(
                        this@PaletteView,
                        paletteValue.setValue(
                                colorPicker.getHue(),
                                colorPicker.getSaturation()
                        )
                )
            }
        }

    private var isChanging = false

    class PaletteValue {

        var hue: Float = 0f
            private set

        var saturation: Float = 0f
            private set

        var set = false
            private set

        fun setValue(hue: Float, saturation: Float): PaletteValue {
            this.hue = hue
            this.saturation = saturation
            this.set = true
            return this
        }

        fun reset(): PaletteValue {
            this.set = false
            return this
        }

    }

    private val paletteValue = PaletteValue()

    init {
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        palettePainter?.onSizeChanged(
                this,
                w,
                h
        )

        palettePainter?.updateByValue(
                this,
                paletteValue.setValue(
                        colorPicker.getHue(),
                        colorPicker.getSaturation()
                )
        )
    }

    override fun onDraw(canvas: Canvas) {
        palettePainter?.onDraw(
                this,
                canvas,
                colorPicker.getColor(),
                paletteValue,
                isChanging
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        palettePainter ?: return super.onTouchEvent(event)

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
            addCallback(this@PaletteView)
        }
    }

    private fun update(
            x: Float,
            y: Float,
            propagate: Boolean
    ) {
        val painter = palettePainter ?: return

        painter.onUpdate(this, x, y, paletteValue.reset())

        if (!paletteValue.set) {
            throw java.lang.IllegalStateException("PaletteView{$this}: ${painter.javaClass.name}" +
                    "#onUpdate did not update hue and saturation by calling" +
                    " PaletteValue#setValue(Float, Float)"
            )
        }

        colorPicker
                .beginTransaction()
                .hue(paletteValue.hue)
                .saturation(paletteValue.saturation)
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

    override fun setValue(value: PaletteValue) {
        this.paletteValue.setValue(
                value.hue,
                value.saturation
        )
    }
}