package me.daemon.colorpicker

import android.graphics.Color

/**
 * @author daemon
 * @since 2019-02-17 00:52
 */
internal class ColorPicker(colorPickerView: ColorPickerView) {

    private var alpha: Float = 0f

    /**
     * * hsv[0]: hue
     * * hsv[1]: saturation
     * * hsv[2]: brightness
     */
    private val hsv = FloatArray(3)

    private val transaction = Transaction(this)

    init {
        val color = colorPickerView.initialColor
        Color.colorToHSV(color, hsv)
        alpha = Color.alpha(color) / 255f
    }

    fun beginTransaction(): Transaction {
        return transaction.begin()
    }

    fun getColor(): Int {
        val alphaInt: Int = (alpha * 255).toInt()
        return Color.HSVToColor(alphaInt, hsv)
    }

    private fun compose(transaction: Transaction, propagate: Boolean) {
        for (op in transaction.ops) {
            when (op.command) {
                Factor.HUE -> this.hsv[0] = Math.max(0f, Math.min(1f, op.value))
                Factor.SATURATION -> this.hsv[1] = Math.max(0f, Math.min(1f, op.value))
                Factor.BRIGHTNESS -> this.hsv[2] = Math.max(0f, Math.min(1f, op.value))
                Factor.ALPHA -> this.alpha = Math.max(0f, Math.min(1f, op.value))
            }
        }

        if (propagate) {
            propagate(getColor())
        }
    }

    private fun propagate(color: Int) {
    }

    enum class Factor {
        HUE,
        SATURATION,
        BRIGHTNESS,
        ALPHA
    }

    class Op(val command: Factor) {

        var value: Float = 0f

        fun value(value: Float): Op {
            this.value = value
            return this
        }
    }

    internal class Transaction(private val colorPicker: ColorPicker) {

        val ops = ArrayList<Op>()

        private val hueOp = Op(Factor.HUE)
        private val saturationOp = Op(Factor.SATURATION)
        private val brightnessOp = Op(Factor.BRIGHTNESS)
        private val alphaOp = Op(Factor.ALPHA)

        var committing = false

        fun begin(): Transaction {
            if (committing) {
                throw IllegalStateException("last transaction not been committed")
            }
            committing = true
            ops.clear()
            return this
        }

        fun hue(hue: Float): Transaction {
            ops.add(hueOp.value(hue))
            return this
        }

        fun saturation(saturation: Float): Transaction {
            ops.add(saturationOp.value(saturation))
            return this
        }

        fun brightness(brightness: Float): Transaction {
            ops.add(brightnessOp.value(brightness))
            return this
        }

        fun alpha(alpha: Float): Transaction {
            ops.add(alphaOp.value(alpha))
            return this
        }

        fun commit(propagate: Boolean) {
            colorPicker.compose(this, propagate)
            committing = false
        }
    }

}