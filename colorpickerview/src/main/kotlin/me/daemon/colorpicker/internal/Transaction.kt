package me.daemon.colorpicker.internal

/**
 * color edit transaction
 *
 * @author daemon
 * @since 2019-02-18 10:55
 */
internal class Transaction(private val colorCenter: ColorCenter) {

    val factors = ArrayList<Factor>()

    private var committing = false

    internal fun begin() = apply {
        if (committing) {
            throw IllegalStateException("last transaction not been committed")
        }
        committing = true
        factors.clear()
    }

    fun hue(hue: Float) = apply {
        if (hue != colorCenter.getHue()) {
            factors.add(Factor.HUE.value(hue))
        }
    }

    fun saturation(saturation: Float) = apply {
        if (saturation != colorCenter.getSaturation()) {
            factors.add(Factor.SATURATION.value(saturation))
        }
    }

    fun brightness(brightness: Float) = apply {
        if (brightness != colorCenter.getBrightness()) {
            factors.add(Factor.BRIGHTNESS.value(brightness))
        }
    }

    fun alpha(alpha: Float) = apply {
        if (alpha != colorCenter.getAlpha()) {
            factors.add(Factor.ALPHA.value(alpha))
        }
    }

    fun commit(propagate: Boolean, force: Boolean) {
        if (force || factors.size > 0) {
            colorCenter.commit(propagate)
        }
        committing = false
    }
}