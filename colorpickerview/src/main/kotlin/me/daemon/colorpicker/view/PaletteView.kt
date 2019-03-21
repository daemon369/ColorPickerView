package me.daemon.colorpicker.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import me.daemon.colorpicker.internal.Callback
import me.daemon.colorpicker.internal.ColorPicker
import me.daemon.colorpicker.painter.IPalettePainter

/**
 * @author daemon
 * @since 2019-02-23 18:57
 */
class PaletteView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Callback, IView<PaletteView.PaletteValue> {

    private lateinit var colorPicker: ColorPicker

    var painter: IPalettePainter? = null
        /**
         * set custom palette painter
         *
         * 设置自定义调色板绘制器
         *
         * @param painter custom palette painter
         *                       调色板绘制器
         */
        set(painter) {
            field = painter?.apply {
                updateByValue(
                        this@PaletteView
                )
            }
        }

    private var isChanging = false

    class PaletteValue : IView.Value() {

        var hue: Float = 0f
            private set

        var saturation: Float = 0f
            private set

        fun setValue(hue: Float, saturation: Float): PaletteValue {
            this.hue = hue
            this.saturation = saturation
            set()
            return this
        }
    }

    private val paletteValue = PaletteValue()

    init {
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        painter?.onSizeChanged(
                this,
                w,
                h
        )

        painter?.updateByValue(
                this
        )
    }

    override fun onDraw(canvas: Canvas) {
        painter?.onDraw(
                this,
                canvas,
                isChanging
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        painter ?: return super.onTouchEvent(event)

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
        val painter = painter ?: return

        paletteValue.reset()
        painter.onUpdate(this, x, y)

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
    }

    override fun callback(
            color: Int,
            hue: Float,
            saturation: Float,
            brightness: Float,
            alpha: Float
    ) {
        paletteValue.setValue(hue, saturation)
        painter?.onColorChanged(this, color)
        invalidate()
    }

    override fun getValue(): PaletteValue {
        return paletteValue
    }

    override fun getColor(): Int {
        return colorPicker.getColor()
    }

}