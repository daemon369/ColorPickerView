package me.daemon.colorpicker

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import me.daemon.colorpicker.internal.Callback
import me.daemon.colorpicker.internal.ColorPicker

/**
 * @author daemon
 * @since 2019-02-26 21:53
 */
class ColorPickerView1 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), Callback {

    private val colorPicker = ColorPicker().apply { addCallback(this@ColorPickerView1) }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}