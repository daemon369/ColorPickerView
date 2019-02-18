package me.daemon.colorpicker.painter

import android.graphics.*
import me.daemon.colorpicker.ColorPickerView

/**
 * @author daemon
 * @since 2019-02-11 20:29
 */
open class DefaultPalettePainter : PalettePainter {

    private val huePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val saturationPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onSizeChanged(w: Int, h: Int, radius: Int, centerX: Int, centerY: Int) {
        val hueShader = SweepGradient(centerX.toFloat(), centerY.toFloat(),
                intArrayOf(Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED),
                null)
        huePaint.shader = hueShader

        val saturationShader = RadialGradient(centerX.toFloat(), centerY.toFloat(), radius.toFloat(),
                Color.WHITE, 0x00FFFFFF, Shader.TileMode.CLAMP)
        saturationPaint.shader = saturationShader

    }

    override fun drawPalette(colorPickerView: ColorPickerView, canvas: Canvas, radius: Int, centerX: Int, centerY: Int) {
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), huePaint)
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), saturationPaint)
    }
}
