package me.daemon.colorpicker.painter

import android.graphics.*
import me.daemon.colorpicker.view.PaletteView

/**
 * @author daemon
 * @since 2019-02-24 00:09
 */
class DefaultPalettePainter(override var indicatorPainter: IndicatorPainter? = null)
    : PalettePainter {

    private var paletteCenterX: Int = 0
    private var paletteCenterY: Int = 0
    private var paletteRadius: Int = 0

    private val currentPoint: PointF = PointF()

    private val huePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val saturationPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)


    private val indicatorRadius = 20

    init {
        indicatorPaint.strokeWidth = 2f
    }

    override fun onSizeChanged(
            view: PaletteView,
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

    override fun onDrawPalette(
            paletteView: PaletteView,
            canvas: Canvas,
            isChanging: Boolean
    ) {
        canvas.drawCircle(paletteCenterX.toFloat(), paletteCenterY.toFloat(), paletteRadius.toFloat(), huePaint)
        canvas.drawCircle(paletteCenterX.toFloat(), paletteCenterY.toFloat(), paletteRadius.toFloat(), saturationPaint)
    }

    override fun onDrawIndicator(
            paletteView: PaletteView,
            canvas: Canvas,
            color: Int,
            isChanging: Boolean
    ) {
        indicatorPaint.color = Color.BLACK

        canvas.drawLine(
                currentPoint.x - indicatorRadius,
                currentPoint.y,
                currentPoint.x + indicatorRadius,
                currentPoint.y,
                indicatorPaint)
        canvas.drawLine(
                currentPoint.x,
                currentPoint.y - indicatorRadius,
                currentPoint.x,
                currentPoint.y + indicatorRadius,
                indicatorPaint)
    }

    override fun onUpdate(
            view: PaletteView,
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

        currentPoint.apply {
            this.x = xReal + paletteCenterX
            this.y = yReal + paletteCenterY
        }

        val hue = (Math.atan2(yReal.toDouble(), (-xReal).toDouble()) / Math.PI * 180f).toFloat() + 180
        val saturation = Math.max(0f, Math.min(1f, (r / paletteRadius).toFloat()))

        view.getValue().setValue(hue, saturation)
    }

    override fun updateByValue(
            view: PaletteView
    ) {
        val value = view.getValue()
        val r = value.saturation * paletteRadius
        val radian = (value.hue / 180f * Math.PI).toFloat()

        currentPoint.apply {
            x = (r * Math.cos(radian.toDouble()) + paletteCenterX).toFloat()
            y = (-r * Math.sin(radian.toDouble()) + paletteCenterY).toFloat()
        }
    }
}