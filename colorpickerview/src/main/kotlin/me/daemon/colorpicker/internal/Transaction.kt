package me.daemon.colorpicker.internal

/**
 * color edit transaction
 *
 * @author daemon
 * @since 2019-02-18 10:55
 */
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