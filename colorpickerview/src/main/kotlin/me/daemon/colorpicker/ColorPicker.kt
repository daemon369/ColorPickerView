package me.daemon.colorpicker

import android.graphics.Color

/**
 * @author daemon
 * @since 2019-02-17 00:52
 */
internal class ColorPicker(val colorPickerView: ColorPickerView) {

    private var color = colorPickerView.initialColor

    private var hue: Float = 0f
    private var saturation: Float = 0f
    private var brightness: Float = 0f
    private var alpha: Float = 0f

    private val hsv = FloatArray(3)

    private val transaction = Transaction(this)

    init {
        Color.colorToHSV(color, hsv)
        hue = hsv[0]
        saturation = hsv[1]
        brightness = hsv[2]
        alpha = Color.alpha(color) / 255f
    }

    fun beginTransaction(): Transaction {
        return transaction.begin()
    }

    private fun compose(transaction: Transaction, propagate: Boolean) {
        for (op in transaction.ops) {
            when (op.command) {
                Factor.HUE -> this.hue = Math.max(0f, Math.min(1f, op.value))
                Factor.SATURATION -> this.saturation = Math.max(0f, Math.min(1f, op.value))
                Factor.BRIGHTNESS -> this.brightness = Math.max(0f, Math.min(1f, op.value))
                Factor.ALPHA -> this.alpha = Math.max(0f, Math.min(1f, op.value))
            }
        }

        hsv[0] = hue
        hsv[1] = saturation
        hsv[2] = brightness

        val alphaInt: Int = (alpha * 255).toInt()

        color = Color.HSVToColor(alphaInt, hsv)
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