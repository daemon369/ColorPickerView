package me.daemon.colorpicker

import android.graphics.Color

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

    private fun compose(propagate: Boolean) {
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

    enum class Factor {
        HUE,
        SATURATION,
        BRIGHTNESS,
        ALPHA;

        var value = 0f

        fun value(value: Float): Factor {
            this.value = value
            return this
        }
    }

    internal class Transaction(private val colorPicker: ColorPicker) {

        val factors = ArrayList<Factor>()

        var committing = false

        fun begin(): Transaction {
            if (committing) {
                throw IllegalStateException("last transaction not been committed")
            }
            committing = true
            factors.clear()
            return this
        }

        fun hue(hue: Float): Transaction {
            factors.add(Factor.HUE.value(hue))
            return this
        }

        fun saturation(saturation: Float): Transaction {
            factors.add(Factor.SATURATION.value(saturation))
            return this
        }

        fun brightness(brightness: Float): Transaction {
            factors.add(Factor.BRIGHTNESS.value(brightness))
            return this
        }

        fun alpha(alpha: Float): Transaction {
            factors.add(Factor.ALPHA.value(alpha))
            return this
        }

        fun commit(propagate: Boolean) {
            colorPicker.compose(propagate)
            committing = false
        }
    }

}