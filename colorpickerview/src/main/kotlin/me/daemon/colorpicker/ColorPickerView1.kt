package me.daemon.colorpicker

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import me.daemon.colorpicker.internal.Callback
import me.daemon.colorpicker.internal.ColorPicker
import me.daemon.colorpicker.painter.PalettePainter1

/**
 * @author daemon
 * @since 2019-02-26 21:53
 */
class ColorPickerView1 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), ColorObservable, Callback {

    private val colorPicker = ColorPicker().apply { addCallback(this@ColorPickerView1) }

    private val paletteView = PaletteView(context).apply {
        setColorPicker(colorPicker)
    }

    private var isAddingInternal = false

    init {
        addViewInternal(paletteView)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    private fun addViewInternal(child: View) {
        isAddingInternal = true
        super.addView(child)
        isAddingInternal = false
    }

    override fun addView(child: View?) {
        if (isAddingInternal) {
            super.addView(child)
        }
    }

    override fun addView(child: View?, index: Int) {
        if (isAddingInternal) {
            super.addView(child, index)
        }
    }

    override fun addView(child: View?, width: Int, height: Int) {
        if (isAddingInternal) {
            super.addView(child, width, height)
        }
    }

    override fun addView(child: View?, params: LayoutParams?) {
        if (isAddingInternal) {
            super.addView(child, params)
        }
    }

    override fun addView(child: View?, index: Int, params: LayoutParams?) {
        if (isAddingInternal) {
            super.addView(child, index, params)
        }
    }

    override fun removeView(view: View?) {
    }

    override fun removeViewAt(index: Int) {
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun callback(
            hue: Float,
            saturation: Float,
            brightness: Float,
            alpha: Float
    ) {
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
        paletteView.setPalettePainter(palettePainter)
    }

    override fun subscribe(observer: ColorObserver) {
        colorPicker.subscribe(observer)
    }

    override fun unsubscribe(observer: ColorObserver) {
        colorPicker.unsubscribe(observer)
    }

    override fun getColor(): Int {
        return colorPicker.getColor()
    }

}