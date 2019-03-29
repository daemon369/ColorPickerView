package me.daemon.colorpicker.internal

import android.graphics.Color
import me.daemon.colorpicker.ColorObservable
import me.daemon.colorpicker.ColorObserver

/**
 * internal color center
 *
 * @author daemon
 * @since 2019-02-17 00:52
 */
internal class ColorCenter : ColorObservable {

    private var alpha: Float = 0f

    /**
     * * hsv[0]: hue
     * * hsv[1]: saturation
     * * hsv[2]: brightness
     */
    private val hsv = FloatArray(3)

    /**
     * Start a series of edit operations on the color
     */
    private val transaction = Transaction(this)

    private val observers = ArrayList<ColorObserver>()

    private val callbacks = ArrayList<Callback>()

    fun beginTransaction() = transaction.begin()

    override fun getColor(): Int {
        val alphaInt: Int = (alpha * 255).toInt()
        return Color.HSVToColor(alphaInt, hsv)
    }

    fun setColor(color: Int, propagate: Boolean) {
        Color.colorToHSV(color, hsv)
        alpha = Color.alpha(color) / 255f

        onColorChange(color, propagate)
    }

    fun getHue() = hsv[0]

    fun getSaturation() = hsv[1]

    fun getBrightness() = hsv[2]

    fun getAlpha() = alpha

    override fun subscribe(observer: ColorObserver) {
        observers.add(observer)
    }

    override fun unsubscribe(observer: ColorObserver) {
        observers.remove(observer)
    }

    fun addCallback(callback: Callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback)
        }
    }

    fun removeCallback(callback: Callback) = callbacks.remove(callback)

    fun removeCallbacks() = callbacks.clear()

    fun commit(propagate: Boolean) {
        transaction.factors.forEach {
            when (it) {
                Factor.HUE -> this.hsv[0] = it.value
                Factor.SATURATION -> this.hsv[1] = Math.max(0f, Math.min(1f, it.value))
                Factor.BRIGHTNESS -> this.hsv[2] = Math.max(0f, Math.min(1f, it.value))
                Factor.ALPHA -> this.alpha = Math.max(0f, Math.min(1f, it.value))
            }
        }

        onColorChange(
                getColor(),
                propagate
        )
    }

    private fun onColorChange(
            color: Int,
            propagate: Boolean
    ) {
        callbacks.forEach {
            it.callback(
                    color,
                    getHue(),
                    getSaturation(),
                    getBrightness(),
                    getAlpha()
            )
        }

        if (propagate) {
            observers.forEach {
                it.onColor(color)
            }
        }

    }
}