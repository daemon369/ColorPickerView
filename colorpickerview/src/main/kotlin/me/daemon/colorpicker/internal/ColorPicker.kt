package me.daemon.colorpicker.internal

import android.graphics.Color
import me.daemon.colorpicker.ColorObservable
import me.daemon.colorpicker.ColorObserver

/**
 * @author daemon
 * @since 2019-02-17 00:52
 */
internal class ColorPicker : ColorObservable {

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

    fun beginTransaction(): Transaction {
        return transaction.begin()
    }

    override fun getColor(): Int {
        val alphaInt: Int = (alpha * 255).toInt()
        return Color.HSVToColor(alphaInt, hsv)
    }

    fun setColor(color: Int) {
        Color.colorToHSV(color, hsv)
        alpha = Color.alpha(color) / 255f
    }

    fun setColor(color: Int, propagate: Boolean) {
        setColor(color)

        if (propagate) {
            propagate(color)
        }
    }

    override fun subscribe(observer: ColorObserver) {
        observers.add(observer)
    }

    override fun unsubscribe(observer: ColorObserver) {
        observers.remove(observer)
    }

    fun compose(propagate: Boolean) {
        for (factor in transaction.factors) {
            when (factor) {
                Factor.HUE -> this.hsv[0] = Math.max(0f, Math.min(1f, factor.value))
                Factor.SATURATION -> this.hsv[1] = Math.max(0f, Math.min(1f, factor.value))
                Factor.BRIGHTNESS -> this.hsv[2] = Math.max(0f, Math.min(1f, factor.value))
                Factor.ALPHA -> this.alpha = Math.max(0f, Math.min(1f, factor.value))
            }
        }

        if (propagate) {
            propagate(getColor())
        }
    }

    private fun propagate(color: Int) {
        for (observer in observers) {
            observer.onColor(color)
        }
    }

}