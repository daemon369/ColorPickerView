package me.daemon.colorpicker.painter

import android.graphics.*
import me.daemon.colorpicker.PaletteView

/**
 * @author daemon
 * @since 2019-02-24 00:09
 */
class DefaultPalettePainter1 : PalettePainter1 {

    private var paletteCenterX: Int = 0
    private var paletteCenterY: Int = 0
    private var paletteRadius: Int = 0

    private val currentPoint: PointF = PointF()

    private val huePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val saturationPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onSizeChanged(
            paletteView: PaletteView,
            w: Int,
            h: Int
    ) {
        paletteCenterX = w / 2
        paletteCenterY = h / 2
        paletteRadius = Math.max(0, Math.min(paletteCenterX, paletteCenterY))

        huePaint.shader = SweepGradient(
                paletteCenterX.toFloat(),
                paletteCenterY.toFloat(),
                intArrayOf(
                        Color.RED,
                        Color.MAGENTA,
                        Color.BLUE,
                        Color.CYAN,
                        Color.GREEN,
                        Color.YELLOW,
                        Color.RED
                ),
                null
        )

        saturationPaint.shader = RadialGradient(
                paletteCenterX.toFloat(),
                paletteCenterY.toFloat(),
                paletteRadius.toFloat(),
                Color.WHITE,
                0x00FFFFFF,
                Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(
            paletteView: PaletteView,
            canvas: Canvas,
            isChanging: Boolean
    ) {
        canvas.drawCircle(paletteCenterX.toFloat(), paletteCenterY.toFloat(), paletteRadius.toFloat(), huePaint)
        canvas.drawCircle(paletteCenterX.toFloat(), paletteCenterY.toFloat(), paletteRadius.toFloat(), saturationPaint)
    }

    override fun onUpdate(
            paletteView: PaletteView,
            x: Float,
            y: Float
    ) {
        var xReal = x - paletteCenterX
        var yReal = y - paletteCenterY
        val r = Math.sqrt((xReal * xReal + yReal * yReal).toDouble())

        if (r > paletteRadius) {
            val ratio = (paletteRadius / r).toFloat()
            xReal *= ratio
            yReal *= ratio
        }

        currentPoint.x = xReal + paletteCenterX
        currentPoint.y = yReal + paletteCenterY
    }
}